const API_PACIENTES = '/api/pacientes';
const API_MEDICOS   = '/api/medicos';
const API_AGENDAS   = '/agendas';

let agendamentos = [];
let paginaAtual = 1;
const itensPorPagina = 5;

let pacientesCache = [];
let medicosCache = [];

document.addEventListener('DOMContentLoaded', () => {
  carregarPacientes();
  carregarMedicos();
  carregarAgendamentos();

  document.getElementById('agendaForm').addEventListener('submit', agendarConsulta);
  document.getElementById('btnCancelarEdicao').addEventListener('click', cancelarEdicao);
  document.getElementById('prevPage').addEventListener('click', () => mudarPagina(-1));
  document.getElementById('nextPage').addEventListener('click', () => mudarPagina(1));
});

function carregarPacientes() {
  const select = document.getElementById('paciente');
  select.innerHTML = '<option value="">Selecione um paciente</option>';

  fetch(API_PACIENTES)
    .then(res => res.json())
    .then(data => {
      const pacientes = Array.isArray(data) ? data : (data.content || []);
      pacientesCache = pacientes;
      if (!pacientes.length) {
        select.innerHTML = '<option value="">Nenhum paciente cadastrado</option>';
        select.disabled = true;
        return;
      }

      select.disabled = false;

      pacientes.forEach(p => {
        const op = document.createElement('option');
        op.value = p.id;
        op.textContent = `${p.nome} ${p.email ? `(${p.email})` : ''}`;
        select.appendChild(op);
      });
    })
    .catch(() => {
      pacientesCache = [];
      select.innerHTML = '<option value="">Erro ao carregar pacientes</option>';
      select.disabled = true;
    });
}

function carregarMedicos() {
  const select = document.getElementById('medico');
  select.innerHTML = '<option value="">Selecione um médico</option>';

  fetch(API_MEDICOS)
    .then(res => res.json())
    .then(data => {
      const medicos = Array.isArray(data) ? data : (data.content || []);
      medicosCache = medicos;
      if (!medicos.length) {
        select.innerHTML = '<option value="">Nenhum médico cadastrado</option>';
        select.disabled = true;
        return;
      }

      select.disabled = false;

      medicos.forEach(med => {
        const op = document.createElement('option');
        op.value = med.id;
        const esp = Array.isArray(med.especialidades)
          ? med.especialidades.map(e => e.nome ?? e).join(', ')
          : (med.especialidades || '');
        op.textContent = esp ? `${med.nome} - ${esp}` : med.nome;
        select.appendChild(op);
      });
    })
    .catch(() => {
      medicosCache = [];
      select.innerHTML = '<option value="">Erro ao carregar médicos</option>';
      select.disabled = true;
    });
}

function carregarAgendamentos() {
  const lista = document.getElementById('listaAgendamentos');
  lista.innerHTML = '<li>Carregando...</li>';

  fetch(API_AGENDAS)
    .then(res => res.json())
    .then(data => {
      agendamentos = Array.isArray(data) ? data : (data.content || []);
      exibirAgendamentos();
    })
    .catch(() => {
      lista.innerHTML = '<li>Erro ao carregar agendamentos</li>';
    });
}

function exibirAgendamentos() {
  const lista = document.getElementById('listaAgendamentos');
  lista.innerHTML = '';

  const inicio = (paginaAtual - 1) * itensPorPagina;
  const fim = inicio + itensPorPagina;
  const pagina = agendamentos.slice(inicio, fim);

  if (!pagina.length) {
    lista.innerHTML = '<li>Nenhum agendamento encontrado.</li>';
    document.getElementById('pageInfo').textContent = '';
    return;
  }

  pagina.forEach(a => {
    const li = document.createElement('li');
    const info = document.createElement('div');
    const status = (a.status || 'AGENDADA').toUpperCase();
    const statusClasse = obterClasseStatus(status);
    const statusTexto = traduzirStatus(status);

    info.innerHTML = `
      <div>
        <strong>${a.pacienteNome}</strong> - ${a.medicoNome}<br>
        <small>${formatarDataParaLista(a.dataHora)}</small> |
        <em>${a.tipoConsulta}</em> | <span class="status ${statusClasse}">${statusTexto}</span>
      </div>
    `;

    const actions = document.createElement('div');
    actions.className = 'list-actions';

    if (status !== 'CANCELADA') {
      const editarBtn = document.createElement('button');
      editarBtn.type = 'button';
      editarBtn.className = 'btn-action';
      editarBtn.textContent = 'Editar';
      editarBtn.addEventListener('click', () => prepararEdicao(a));
      actions.appendChild(editarBtn);
    }

    if (status === 'AGENDADA') {
      const cancelarBtn = document.createElement('button');
      cancelarBtn.type = 'button';
      cancelarBtn.className = 'btn-action btn-action-danger';
      cancelarBtn.textContent = 'Cancelar';
      cancelarBtn.addEventListener('click', () => cancelarAgendamento(a.id));
      actions.appendChild(cancelarBtn);
    }

    li.appendChild(info);
    if (actions.children.length) {
      li.appendChild(actions);
    }
    lista.appendChild(li);
  });

  const totalPaginas = Math.ceil(agendamentos.length / itensPorPagina);
  document.getElementById('pageInfo').textContent = `Página ${paginaAtual} de ${totalPaginas}`;
}

function mudarPagina(direcao) {
  const totalPaginas = Math.ceil(agendamentos.length / itensPorPagina);
  if (paginaAtual + direcao >= 1 && paginaAtual + direcao <= totalPaginas) {
    paginaAtual += direcao;
    exibirAgendamentos();
  }
}

function agendarConsulta(event) {
  event.preventDefault();

  const pacienteId  = document.getElementById('paciente').value;
  const medicoId    = document.getElementById('medico').value;
  const tipoConsulta = document.getElementById('tipoConsulta').value;
  const dataHora     = document.getElementById('dataHora').value;
  const agendamentoId = document.getElementById('agendamentoId').value;

  if (!agendamentoId && !pacienteId) {
    alert('Selecione um paciente.');
    return;
  }

  if (!agendamentoId && (!medicoId || isNaN(medicoId))) {
    alert('Selecione um médico válido.');
    return;
  }

  if (!dataHora) {
    alert('Informe a data e a hora.');
    return;
  }

  if (agendamentoId) {
    remarcarAgendamento(agendamentoId, dataHora, tipoConsulta);
    return;
  }

  const payload = {
    pacienteId: Number(pacienteId),
    medicoId: Number(medicoId),
    tipoConsulta,
    dataHora
  };

  fetch(`${API_AGENDAS}/agendar`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  })
    .then(res => {
      if (!res.ok) return res.json().then(err => Promise.reject(err));
      return res.json();
    })
    .then(() => {
      alert('Consulta agendada com sucesso!');
      resetFormulario();
      carregarAgendamentos();
    })
    .catch(err => {
      alert(err.message || 'Erro ao agendar consulta.');
    });
}

function remarcarAgendamento(id, dataHora, tipoConsulta) {
  const params = new URLSearchParams({
    novaDataHora: dataHora,
    tipoConsulta
  });

  fetch(`${API_AGENDAS}/${id}/remarcar?${params.toString()}`, {
    method: 'PUT'
  })
    .then(res => {
      if (!res.ok) return res.json().then(err => Promise.reject(err));
      return res.json();
    })
    .then(() => {
      alert('Agendamento atualizado com sucesso!');
      resetFormulario();
      carregarAgendamentos();
    })
    .catch(err => {
      alert(err.message || 'Erro ao atualizar agendamento.');
    });
}

function prepararEdicao(agendamento) {
  document.getElementById('agendamentoId').value = agendamento.id;
  document.getElementById('btnAgendar').textContent = 'Salvar alterações';
  document.getElementById('btnCancelarEdicao').classList.remove('hidden');

  selecionarPaciente(agendamento.pacienteNome);
  selecionarMedico(agendamento.medicoNome);

  document.getElementById('tipoConsulta').value = agendamento.tipoConsulta || 'PRESENCIAL';
  document.getElementById('dataHora').value = formatarDataParaInput(agendamento.dataHora);

  document.getElementById('paciente').disabled = true;
  document.getElementById('medico').disabled = true;
}

function selecionarPaciente(nome) {
  const select = document.getElementById('paciente');
  const paciente = pacientesCache.find(p => (p.nome || '').toLowerCase() === (nome || '').toLowerCase());
  if (paciente) {
    select.value = paciente.id;
  }
}

function selecionarMedico(nome) {
  const select = document.getElementById('medico');
  const medico = medicosCache.find(m => (m.nome || '').toLowerCase() === (nome || '').toLowerCase());
  if (medico) {
    select.value = medico.id;
  }
}

function cancelarEdicao() {
  resetFormulario();
}

function resetFormulario() {
  document.getElementById('agendaForm').reset();
  document.getElementById('agendamentoId').value = '';
  document.getElementById('btnAgendar').textContent = 'Agendar';
  document.getElementById('btnCancelarEdicao').classList.add('hidden');
  document.getElementById('paciente').disabled = false;
  document.getElementById('medico').disabled = false;
}

function formatarDataParaLista(dataHora) {
  if (!dataHora) return '';
  const data = new Date(dataHora);
  return data.toLocaleString('pt-BR');
}

function formatarDataParaInput(dataHora) {
  if (!dataHora) return '';
  const data = new Date(dataHora);
  const timezoneOffset = data.getTimezoneOffset() * 60000;
  const localISOTime = new Date(data.getTime() - timezoneOffset).toISOString();
  return localISOTime.slice(0, 16);
}

function cancelarAgendamento(id) {
  if (!id) return;

  const confirmacao = confirm('Tem certeza de que deseja cancelar este agendamento?');
  if (!confirmacao) {
    return;
  }

  fetch(`${API_AGENDAS}/${id}/cancelar`, {
    method: 'PATCH'
  })
    .then(res => {
      if (!res.ok) {
        return res.json().then(err => Promise.reject(err));
      }
      return res.json();
    })
    .then(() => {
      alert('Agendamento cancelado com sucesso!');
      resetFormulario();
      carregarAgendamentos();
    })
    .catch(err => {
      alert(err.message || 'Erro ao cancelar agendamento.');
    });
}

function obterClasseStatus(status) {
  if (!status) return '';
  return `status-${status.toLowerCase()}`;
}

function traduzirStatus(status) {
  const mapa = {
    AGENDADA: 'Agendada',
    CANCELADA: 'Cancelada',
    CONCLUIDA: 'Concluída'
  };

  return mapa[status] || status || '';
}
