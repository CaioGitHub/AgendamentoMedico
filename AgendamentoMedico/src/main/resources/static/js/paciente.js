const API_URL = '/api/pacientes';
const API_CONVENIOS = '/api/convenios';

let pacientes = [];
let paginaAtual = 1;
const itensPorPagina = 10;

document.addEventListener('DOMContentLoaded', () => {
    carregarPacientes();
    carregarConvenios();

    document.getElementById('btnSalvar').addEventListener('click', salvarPaciente);
    document.getElementById('prevPage').addEventListener('click', () => mudarPagina(-1));
    document.getElementById('nextPage').addEventListener('click', () => mudarPagina(1));

    document.getElementById('telefonePaciente').addEventListener('input', aplicarMascaraTelefone);
});

async function carregarPacientes() {
    const lista = document.getElementById('listaPacientes');
    lista.innerHTML = '<li>Carregando...</li>';

    try {
        const response = await fetch(API_URL);
        pacientes = await response.json();
        exibirPacientes();
    } catch (error) {
        lista.innerHTML = '<li>Erro ao carregar pacientes</li>';
        console.error(error);
    }
}

async function carregarConvenios() {
    try {
        const response = await fetch(API_CONVENIOS);
        const convenios = await response.json();

        const select = document.getElementById('convenioPaciente');
        convenios.forEach(conv => {
            const option = document.createElement('option');
            option.value = conv.id;
            option.textContent = `${conv.nome} (${conv.cobertura})`;
            select.appendChild(option);
        });
    } catch (error) {
        console.error('Erro ao carregar convênios', error);
    }
}

function exibirPacientes() {
    const lista = document.getElementById('listaPacientes');
    lista.innerHTML = '';

    const inicio = (paginaAtual - 1) * itensPorPagina;
    const fim = inicio + itensPorPagina;
    const pacientesPagina = pacientes.slice(inicio, fim);

    pacientesPagina.forEach(p => {
        const li = document.createElement('li');
        li.innerHTML = `
            <span class="long-text">${p.id} - ${p.nome} (${p.email})</span>
            <div>
                <button class="btn-action" onclick="editar(${p.id})">Editar</button>
                <button class="btn-action" onclick="deletarPaciente(${p.id})">Excluir</button>
            </div>
        `;
        lista.appendChild(li);
    });

    if (pacientesPagina.length === 0) {
        lista.innerHTML = '<li>Nenhum paciente cadastrado.</li>';
    }

    document.getElementById('pageInfo').textContent =
        `Página ${paginaAtual} de ${Math.ceil(pacientes.length / itensPorPagina)}`;
}

function mudarPagina(direcao) {
    const totalPaginas = Math.ceil(pacientes.length / itensPorPagina);
    if (paginaAtual + direcao >= 1 && paginaAtual + direcao <= totalPaginas) {
        paginaAtual += direcao;
        exibirPacientes();
    }
}

async function salvarPaciente() {
    const id = document.getElementById('idPaciente').value;
    const nome = document.getElementById('nomePaciente').value.trim();
    const email = document.getElementById('emailPaciente').value.trim();
    const telefone = document.getElementById('telefonePaciente').value.replace(/\D/g, '');
    const dataNascimento = document.getElementById('dataNascimentoPaciente').value;
    const convenioId = document.getElementById('convenioPaciente').value || null;

    if (!nome || !email || !dataNascimento) {
        alert('Preencha todos os campos obrigatórios!');
        return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        alert('Por favor, insira um e-mail válido!');
        return;
    }

    const paciente = { nome, email, telefone, dataNascimento, convenioId };

    try {
        if (id) {
            await fetch(`${API_URL}/${id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(paciente)
            });
        } else {
            await fetch(API_URL, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(paciente)
            });
        }

        alert('Paciente salvo com sucesso!');
        limparFormulario();
        carregarPacientes();

    } catch (error) {
        console.error(error);
        alert('Erro ao salvar paciente!');
    }
}

function editar(id) {
    const paciente = pacientes.find(p => p.id === id);
    if (!paciente) return;

    document.getElementById('idPaciente').value = paciente.id;
    document.getElementById('nomePaciente').value = paciente.nome;
    document.getElementById('emailPaciente').value = paciente.email;
    document.getElementById('telefonePaciente').value = paciente.telefone;
    document.getElementById('dataNascimentoPaciente').value = paciente.dataNascimento;
    document.getElementById('convenioPaciente').value = paciente.convenio ? paciente.convenio.id : '';
}

async function deletarPaciente(id) {
    if (!confirm('Deseja realmente excluir este paciente?')) return;

    try {
        await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
        alert('Paciente excluído com sucesso!');
        carregarPacientes();
    } catch (error) {
        console.error(error);
        alert('Erro ao excluir paciente!');
    }
}

function limparFormulario() {
    document.getElementById('idPaciente').value = '';
    document.getElementById('nomePaciente').value = '';
    document.getElementById('emailPaciente').value = '';
    document.getElementById('telefonePaciente').value = '';
    document.getElementById('dataNascimentoPaciente').value = '';
    document.getElementById('convenioPaciente').value = '';
}

function aplicarMascaraTelefone(event) {
    let valor = event.target.value.replace(/\D/g, '');
    if (valor.length > 11) valor = valor.slice(0, 11);

    if (valor.length <= 10) {
        valor = valor.replace(/^(\d{2})(\d{4})(\d{0,4})$/, '($1)$2-$3');
    } else {
        valor = valor.replace(/^(\d{2})(\d{5})(\d{0,4})$/, '($1)$2-$3');
    }

    event.target.value = valor;
}
