const API_PACIENTES = '/api/pacientes';
const API_MEDICOS   = '/api/medicos';
const API_AGENDAS   = '/agendas';

let agendamentos = [];
let paginaAtual = 1;
const itensPorPagina = 5;

document.addEventListener('DOMContentLoaded', () => {
  carregarPacientes();
  carregarMedicos();
  carregarAgendamentos();

  const form = document.getElementById('agendaForm');
  if (form) {
    form.addEventListener('submit', agendarConsulta);
  }

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
      if (!pacientes.length) {
        select.innerHTML = '<option value="">Nenhum paciente cadastrado</option>';
        select.disabled = true;
        return;
      }

      pacientes.forEach(p => {
        const op = document.createElement('option');
        op.value = p.id;
        op.textContent = `${p.nome} ${p.email ? `(${p.email})` : ''}`;
        select.appendChild(op);
      });
    })
    .catch(() => {
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
      if (!medicos.length) {
        select.innerHTML = '<option value="">Nenhum médico cadastrado</option>';
        select.disabled = true;
        return;
      }

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
    li.innerHTML = `
      <div>
        <strong>${a.pacienteNome}</strong> - ${a.medicoNome}<br>
        <small>${new Date(a.dataHora).toLocaleString('pt-BR')}</small> |
        <em>${a.tipoConsulta}</em>
      </div>
    `;
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

  if (!pacienteId) {
    alert('Selecione um paciente.');
    return;
  }

  if (!medicoId || isNaN(medicoId)) {
    alert('Selecione um médico válido.');
    return;
  }

  if (!dataHora) {
    alert('Informe a data e a hora.');
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
      resetFormularioAgenda();
      carregarAgendamentos();
    })
    .catch(err => {
      alert(err.message || 'Erro ao agendar consulta.');
    });
}

function resetFormularioAgenda() {
  const form = document.getElementById('agendaForm');
  if (form) {
    form.reset();
  }
}
