const API_MEDICOS = '/api/medicos';
const API_AGENDAS = '/agendas'; // ajuste se sua rota for diferente

document.addEventListener('DOMContentLoaded', () => {
    carregarMedicos();
    document.getElementById('agendaForm').addEventListener('submit', agendarConsulta);
});

// ====================== CARREGAR MÉDICOS ======================
function carregarMedicos() {
    fetch(`${API_MEDICOS}?page=0&size=100`) // carrega até 100 médicos
        .then(res => res.json())
        .then(data => {
            const medicos = data.content || data; // se vier Page ou Array
            const select = document.getElementById('medico');

            select.innerHTML = '<option value="">Selecione um médico</option>';

            medicos.forEach(medico => {
                const option = document.createElement('option');
                option.value = medico.id; // precisa que o DTO inclua ID
                option.textContent = `${medico.nome} - ${medico.especialidades.join(', ')}`;
                select.appendChild(option);
            });
        })
        .catch(err => console.error('Erro ao carregar médicos:', err));
}

// ====================== AGENDAR CONSULTA ======================
function agendarConsulta(event) {
    event.preventDefault();

    const paciente = document.getElementById('paciente').value.trim();
    const medicoId = document.getElementById('medico').value;
    const tipoConsulta = document.getElementById('tipoConsulta').value;
    const dataHora = document.getElementById('dataHora').value;

    if (!paciente || !medicoId || !dataHora) {
        alert('Preencha todos os campos.');
        return;
    }

    const payload = {
        pacienteId: paciente,     // ajuste se usar ID de paciente real
        medicoId: medicoId,
        tipoConsulta: tipoConsulta,
        dataHora: dataHora
    };

    fetch(`${API_AGENDAS}/agendar`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    })
    .then(res => {
        if (!res.ok) throw new Error('Erro ao agendar consulta');
        return res.json();
    })
    .then(() => {
        alert('Consulta agendada com sucesso!');
        document.getElementById('agendaForm').reset();
    })
    .catch(err => alert(err.message));
}
