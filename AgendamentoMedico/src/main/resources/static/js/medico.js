const API_MEDICOS = '/api/medicos';
const API_ESPECIALIDADES = '/api/especialidades';

let paginaAtual = 0;
const itensPorPagina = 5;
let totalPaginas = 1;
let especialidadeSelecionada = '';

document.addEventListener('DOMContentLoaded', () => {
  carregarEspecialidadesForm();
  carregarEspecialidadesFiltro();
  carregarMedicos();

  document.getElementById('medicoForm').addEventListener('submit', salvarMedico);

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
  fetch(API_ESPECIALIDADES)
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
      const medicos = data.content;
      totalPaginas = data.totalPages;
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
      card.innerHTML = `
        <p><strong>Nome:</strong> ${medico.nome}</p>
        <p><strong>CRM:</strong> ${medico.crm}</p>
        <p><strong>Endereço:</strong> ${medico.endereco}</p>
        <p><strong>Especialidades:</strong> ${medico.especialidades.join(', ')}</p>
      `;
      container.appendChild(card);
    });
  }

  pageInfo.textContent = `Página ${paginaAtual + 1} de ${totalPaginas}`;
  prevBtn.disabled = paginaAtual === 0;
  nextBtn.disabled = paginaAtual >= totalPaginas - 1;
}


function salvarMedico(event) {
  event.preventDefault();

  const nome = document.getElementById('nome').value.trim();
  const crm = document.getElementById('crm').value.trim();
  const endereco = document.getElementById('endereco').value.trim();
  const especialidades = Array.from(document.getElementById('especialidades').selectedOptions)
    .map(opt => parseInt(opt.value));

  const medico = { nome, crm, endereco, especialidades };

  fetch(API_MEDICOS, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(medico)
  })
    .then(res => {
      if (!res.ok) throw new Error('Erro ao salvar médico');
      return res.json();
    })
    .then(() => {
      alert('Médico cadastrado com sucesso!');
      document.getElementById('medicoForm').reset();
      especialidadeSelecionada = '';
      const filtro = document.getElementById('filtroEspecialidade');
      if (filtro) filtro.value = '';
      paginaAtual = 0;
      carregarMedicos();
    })
    .catch(err => alert(err.message));
}
