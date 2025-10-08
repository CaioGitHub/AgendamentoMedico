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

      select.disabled = false;
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

  pagina.forEach(agendamento => {
    const li = document.createElement('li');

    const info = document.createElement('div');
    info.className = 'agenda-info';

    const titulo = document.createElement('span');
    titulo.className = 'agenda-paciente';
    titulo.textContent = agendamento.pacienteNome;
    info.appendChild(titulo);

    const meta = document.createElement('div');
    meta.className = 'agenda-meta';

    meta.appendChild(criarMetaItem(agendamento.medicoNome));
    meta.appendChild(criarMetaItem(formatarDataHora(agendamento.dataHora)));
    meta.appendChild(criarMetaItem(formatarTipoConsulta(agendamento.tipoConsulta)));

    if (agendamento.status) {
      const status = document.createElement('span');
      status.className = `agenda-status agenda-status--${String(agendamento.status).toLowerCase()}`;
      status.textContent = formatarStatusAgenda(agendamento.status);
      meta.appendChild(status);
    }

    info.appendChild(meta);

    const actions = document.createElement('div');
    actions.className = 'agenda-actions';

    actions.appendChild(criarBotaoAcao('🗓️ Remarcar', 'btn-action btn-action--primary',
      () => remarcarAgendamento(agendamento)));
    actions.appendChild(criarBotaoAcao('🚫 Cancelar', 'btn-action btn-action--cancel',
      () => cancelarAgendamento(agendamento.id)));
    actions.appendChild(criarBotaoAcao('🗑️ Excluir', 'btn-action btn-action--danger',
      () => excluirAgendamento(agendamento.id)));

    li.appendChild(info);
    li.appendChild(actions);
    lista.appendChild(li);
  });

  const totalPaginas = Math.ceil(agendamentos.length / itensPorPagina);
  document.getElementById('pageInfo').textContent = `Página ${paginaAtual} de ${totalPaginas}`;
}

function criarMetaItem(valor) {
  const span = document.createElement('span');
  span.textContent = valor;
  return span;
}

function criarBotaoAcao(rotulo, classe, handler) {
  const botao = document.createElement('button');
  botao.type = 'button';
  botao.className = classe;
  botao.textContent = rotulo;
  botao.addEventListener('click', handler);
  return botao;
}

function formatarDataHora(data) {
  try {
    return new Intl.DateTimeFormat('pt-BR', {
      dateStyle: 'short',
      timeStyle: 'short'
    }).format(new Date(data));
  } catch (e) {
    return data;
  }
}

function formatarTipoConsulta(tipo) {
  const mapa = {
    PRESENCIAL: 'Presencial',
    REMOTO: 'Remoto'
  };
  return mapa[String(tipo).toUpperCase()] || tipo;
}

function formatarStatusAgenda(status) {
  const mapa = {
    ATIVO: 'Ativo',
    REMARCADO: 'Remarcado',
    CANCELADO: 'Cancelado'
  };
  return mapa[String(status).toUpperCase()] || status;
}

function remarcarAgendamento(agendamento) {
  const valorInicial = formatarParaInputDatetime(agendamento.dataHora);
  const novaDataHora = prompt('Informe a nova data e hora (AAAA-MM-DDTHH:MM):', valorInicial);
  if (!novaDataHora) {
    return;
  }

  const tipoConsulta = prompt('Informe o tipo de consulta (PRESENCIAL ou REMOTO):', agendamento.tipoConsulta);
  if (!tipoConsulta) {
    return;
  }

  const tipoNormalizado = String(tipoConsulta).trim().toUpperCase();

  fetch(`${API_AGENDAS}/${agendamento.id}/remarcar?novaDataHora=${encodeURIComponent(novaDataHora)}&tipoConsulta=${encodeURIComponent(tipoNormalizado)}`, {
    method: 'PUT'
  })
    .then(res => {
      if (!res.ok) {
        return res.json().then(err => Promise.reject(err));
      }
      return res.json();
    })
    .then(() => {
      alert('Agendamento remarcado com sucesso!');
      carregarAgendamentos();
    })
    .catch(err => {
      alert(err?.message || 'Não foi possível remarcar o agendamento.');
    });
}

function cancelarAgendamento(id) {
  if (!confirm('Deseja realmente cancelar este agendamento?')) {
    return;
  }

  fetch(`${API_AGENDAS}/${id}/cancelar`, { method: 'PATCH' })
    .then(res => {
      if (!res.ok) {
        return res.json().then(err => Promise.reject(err));
      }
      return res.json();
    })
    .then(() => {
      alert('Agendamento cancelado com sucesso.');
      carregarAgendamentos();
    })
    .catch(err => {
      alert(err?.message || 'Erro ao cancelar agendamento.');
    });
}

function excluirAgendamento(id) {
  if (!confirm('Confirma a exclusão definitiva deste agendamento?')) {
    return;
  }

  fetch(`${API_AGENDAS}/${id}`, { method: 'DELETE' })
    .then(res => {
      if (!res.ok && res.status !== 204) {
        return res.json().then(err => Promise.reject(err));
      }
    })
    .then(() => {
      alert('Agendamento excluído com sucesso.');
      carregarAgendamentos();
    })
    .catch(err => {
      alert(err?.message || 'Erro ao excluir agendamento.');
    });
}

function formatarParaInputDatetime(valor) {
  if (!valor) {
    return '';
  }

  const data = new Date(valor);
  if (Number.isNaN(data.getTime())) {
    return valor;
  }

  const pad = numero => String(numero).padStart(2, '0');
  const ano = data.getFullYear();
  const mes = pad(data.getMonth() + 1);
  const dia = pad(data.getDate());
  const hora = pad(data.getHours());
  const minuto = pad(data.getMinutes());

  return `${ano}-${mes}-${dia}T${hora}:${minuto}`;
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
