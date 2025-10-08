const API_MEDICOS = '/api/medicos';
const API_ESPECIALIDADES = '/api/especialidades';

let paginaAtual = 0;
const itensPorPagina = 5;
let totalPaginas = 1;
let especialidadeSelecionada = '';
let medicoEmEdicaoId = null;
let medicosCache = [];

document.addEventListener('DOMContentLoaded', () => {
  carregarEspecialidadesForm();
  carregarEspecialidadesFiltro();
  carregarMedicos();

  document.getElementById('medicoForm').addEventListener('submit', salvarMedico);

  const cancelarBtn = document.getElementById('cancelarEdicao');
  if (cancelarBtn) {
    cancelarBtn.addEventListener('click', cancelarEdicao);
  }

  document.getElementById('prevPage').addEventListener('click', () => {
    if (paginaAtual > 0) {
      paginaAtual--;
      carregarMedicos();
    }
  });

  document.getElementById('nextPage').addEventListener('click', () => {
    if (paginaAtual < totalPaginas - 1) {
      paginaAtual++;
      carregarMedicos();
    }
  });

  document.getElementById('filtroEspecialidade').addEventListener('change', (e) => {
    especialidadeSelecionada = e.target.value;
    paginaAtual = 0;
    carregarMedicos();
  });
});

function carregarEspecialidadesForm() {
  return fetch(API_ESPECIALIDADES)
    .then(res => res.json())
    .then(data => {
      const select = document.getElementById('especialidades');
      if (!select) return;
      select.innerHTML = '';
      data.forEach(esp => {
        const option = document.createElement('option');
        option.value = esp.id;
        option.textContent = esp.nome;
        select.appendChild(option);
      });
    })
    .catch(err => console.error('Erro ao carregar especialidades (form):', err));
}

function carregarEspecialidadesFiltro() {
  fetch(API_ESPECIALIDADES)
    .then(res => res.json())
    .then(data => {
      const filtroSelect = document.getElementById('filtroEspecialidade');
      if (!filtroSelect) return;
      filtroSelect.innerHTML = '<option value="">Todas</option>';
      data.forEach(esp => {
        const option = document.createElement('option');
        option.value = esp.nome;
        option.textContent = esp.nome;
        filtroSelect.appendChild(option);
      });
    })
    .catch(err => console.error('Erro ao carregar especialidades (filtro):', err));
}

function carregarMedicos() {
  let url;
  if (especialidadeSelecionada) {
    url = `${API_MEDICOS}/especialidade/${encodeURIComponent(especialidadeSelecionada)}?page=${paginaAtual}&size=${itensPorPagina}`;
  } else {
    url = `${API_MEDICOS}?page=${paginaAtual}&size=${itensPorPagina}`;
  }

  fetch(url)
    .then(res => res.json())
    .then(data => {
      const medicos = data && Array.isArray(data.content) ? data.content : [];
      const paginasRecebidas = data && typeof data.totalPages === 'number' ? data.totalPages : 1;
      totalPaginas = Math.max(paginasRecebidas, 1);

      if (medicos.length === 0 && paginaAtual > 0) {
        paginaAtual = Math.max(0, paginaAtual - 1);
        carregarMedicos();
        return;
      }

      medicosCache = medicos;
      renderizarMedicos(medicos);
    })
    .catch(err => console.error('Erro ao carregar médicos:', err));
}

function renderizarMedicos(medicos) {
  const container = document.getElementById('medicosList');
  const pageInfo = document.getElementById('pageInfo');
  const prevBtn = document.getElementById('prevPage');
  const nextBtn = document.getElementById('nextPage');

  container.innerHTML = '';

  if (!medicos || medicos.length === 0) {
    container.innerHTML = '<p>Nenhum médico encontrado.</p>';
  } else {
    medicos.forEach(medico => {
      const card = document.createElement('div');
      card.classList.add('medico-card');

      const info = document.createElement('div');
      info.classList.add('medico-info');
      info.innerHTML = `
        <p><strong>Nome:</strong> ${medico.nome}</p>
        <p><strong>CRM:</strong> ${medico.crm}</p>
        <p><strong>Endereço:</strong> ${medico.endereco}</p>
        <p><strong>Especialidades:</strong> ${medico.especialidades.join(', ')}</p>
      `;

      const actions = document.createElement('div');
      actions.classList.add('action-buttons');

      const editButton = document.createElement('button');
      editButton.type = 'button';
      editButton.classList.add('btn-secondary');
      editButton.textContent = 'Editar';
      editButton.addEventListener('click', () => iniciarEdicao(medico));

      const deleteButton = document.createElement('button');
      deleteButton.type = 'button';
      deleteButton.classList.add('btn-danger');
      deleteButton.textContent = 'Excluir';
      deleteButton.addEventListener('click', () => excluirMedico(medico.id));

      actions.appendChild(editButton);
      actions.appendChild(deleteButton);

      card.appendChild(info);
      card.appendChild(actions);
      container.appendChild(card);
    });
  }

  pageInfo.textContent = `Página ${Math.min(paginaAtual + 1, totalPaginas)} de ${totalPaginas}`;
  prevBtn.disabled = paginaAtual === 0;
  nextBtn.disabled = paginaAtual >= totalPaginas - 1;
}

function salvarMedico(event) {
  event.preventDefault();

  const nome = document.getElementById('nome').value.trim();
  const crm = document.getElementById('crm').value.trim();
  const endereco = document.getElementById('endereco').value.trim();
  const especialidades = Array.from(document.getElementById('especialidades').selectedOptions)
    .map(opt => parseInt(opt.value, 10));

  const medico = { nome, crm, endereco, especialidades };
  if (medicoEmEdicaoId) {
    medico.id = medicoEmEdicaoId;
  }

  const url = medicoEmEdicaoId ? `${API_MEDICOS}/${medicoEmEdicaoId}` : API_MEDICOS;
  const metodo = medicoEmEdicaoId ? 'PUT' : 'POST';

  fetch(url, {
    method: metodo,
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(medico)
  })
    .then(res => {
      if (!res.ok) throw new Error('Erro ao salvar médico');
      return res.status === 204 ? null : res.json();
    })
    .then(() => {
      if (medicoEmEdicaoId) {
        alert('Médico atualizado com sucesso!');
      } else {
        alert('Médico cadastrado com sucesso!');
        especialidadeSelecionada = '';
        const filtro = document.getElementById('filtroEspecialidade');
        if (filtro) filtro.value = '';
        paginaAtual = 0;
      }
      cancelarEdicao();
      carregarMedicos();
    })
    .catch(err => alert(err.message));
}

function iniciarEdicao(medico) {
  medicoEmEdicaoId = medico.id;
  document.getElementById('formTitle').textContent = 'Editar Médico';
  document.getElementById('submitButton').textContent = 'Atualizar Médico';
  document.getElementById('cancelarEdicao').classList.remove('hidden');

  document.getElementById('nome').value = medico.nome || '';
  document.getElementById('crm').value = medico.crm || '';
  document.getElementById('endereco').value = medico.endereco || '';

  const select = document.getElementById('especialidades');
  if (!select.options.length) {
    carregarEspecialidadesForm().then(() => selecionarEspecialidades(select, medico.especialidades));
  } else {
    selecionarEspecialidades(select, medico.especialidades);
  }
}

function selecionarEspecialidades(select, nomesEspecialidades) {
  const listaNomes = Array.isArray(nomesEspecialidades) ? nomesEspecialidades : [];
  Array.from(select.options).forEach(option => {
    option.selected = listaNomes.includes(option.textContent);
  });
}

function cancelarEdicao() {
  medicoEmEdicaoId = null;
  document.getElementById('formTitle').textContent = 'Novo Médico';
  document.getElementById('submitButton').textContent = 'Salvar Médico';
  document.getElementById('cancelarEdicao').classList.add('hidden');

  const form = document.getElementById('medicoForm');
  form.reset();
  const select = document.getElementById('especialidades');
  Array.from(select.options).forEach(option => {
    option.selected = false;
  });
}

function excluirMedico(id) {
  if (!confirm('Deseja realmente excluir este médico?')) {
    return;
  }

  fetch(`${API_MEDICOS}/${id}`, {
    method: 'DELETE'
  })
    .then(res => {
      if (!res.ok) throw new Error('Erro ao excluir médico');
    })
    .then(() => {
      alert('Médico excluído com sucesso!');
      if (medicoEmEdicaoId === id) {
        cancelarEdicao();
      }
      if (medicosCache.length <= 1 && paginaAtual > 0) {
        paginaAtual--;
      }
      carregarMedicos();
    })
    .catch(err => alert(err.message));
}
