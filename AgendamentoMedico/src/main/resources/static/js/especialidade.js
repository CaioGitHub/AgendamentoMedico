const API_URL = '/api/especialidades';
let currentPage = 1;
const pageSize = 10;
let especialidades = [];
let especialidadeEmEdicaoId = null;

document.addEventListener('DOMContentLoaded', () => {
    carregarEspecialidades();

    const form = document.getElementById('especialidadeForm');
    if (form) {
        form.addEventListener('submit', salvarEspecialidade);
    }

    const cancelarBtn = document.getElementById('btnCancelar');
    if (cancelarBtn) {
        cancelarBtn.addEventListener('click', cancelarEdicaoEspecialidade);
    }

    document.getElementById('prevPage').addEventListener('click', () => mudarPagina(-1));
    document.getElementById('nextPage').addEventListener('click', () => mudarPagina(1));
});

async function carregarEspecialidades() {
    try {
        const response = await fetch(API_URL);
        especialidades = await response.json();
        renderizarLista();
    } catch (error) {
        console.error(error);
        document.getElementById('listaEspecialidades').innerHTML = '<li>Erro ao carregar especialidades</li>';
    }
}

function renderizarLista() {
    const lista = document.getElementById('listaEspecialidades');
    lista.innerHTML = '';

    const start = (currentPage - 1) * pageSize;
    const end = start + pageSize;
    const pagina = especialidades.slice(start, end);

    if (pagina.length === 0) {
        lista.innerHTML = '<li>Nenhuma especialidade cadastrada.</li>';
    } else {
        pagina.forEach(esp => {
            const li = document.createElement('li');
            li.innerHTML = `
                <span>${esp.id} - ${esp.nome}</span>
                <div>
                    <button class="btn-action" onclick="editar(${esp.id}, '${esp.nome}')">Editar</button>
                    <button class="btn-action" onclick="deletarEspecialidade(${esp.id})">Excluir</button>
                </div>
            `;
            lista.appendChild(li);
        });
    }

    atualizarPaginacao();
}

function atualizarPaginacao() {
    const totalPaginas = Math.ceil(especialidades.length / pageSize);
    document.getElementById('pageInfo').textContent = `Página ${currentPage} de ${totalPaginas || 1}`;

    document.getElementById('prevPage').disabled = currentPage === 1;
    document.getElementById('nextPage').disabled = currentPage === totalPaginas || totalPaginas === 0;
}

function mudarPagina(delta) {
    currentPage += delta;
    renderizarLista();
}

async function salvarEspecialidade(event) {
    event.preventDefault();

    const id = especialidadeEmEdicaoId;
    const nome = document.getElementById('nomeEspecialidade').value.trim();

    if (!nome) {
        alert('Informe o nome da especialidade!');
        return;
    }

    const especialidade = { nome };

    try {
        if (id) {
            await fetch(`${API_URL}/${id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(especialidade)
            });
        } else {
            await fetch(API_URL, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(especialidade)
            });
        }

        alert(id ? 'Especialidade atualizada com sucesso!' : 'Especialidade salva com sucesso!');
        cancelarEdicaoEspecialidade();
        await carregarEspecialidades();

    } catch (error) {
        console.error(error);
        alert('Erro ao salvar a especialidade!');
        if (especialidadeEmEdicaoId) {
            cancelarEdicaoEspecialidade();
        } else {
            resetFormularioEspecialidade();
        }
    }
}

function editar(id, nome) {
    especialidadeEmEdicaoId = id;
    document.getElementById('formTitle').textContent = 'Editar Especialidade';
    document.getElementById('btnSalvar').textContent = 'Atualizar Especialidade';
    document.getElementById('btnCancelar').classList.remove('hidden');

    document.getElementById('idEspecialidade').value = id;
    document.getElementById('nomeEspecialidade').value = nome;
}

async function deletarEspecialidade(id) {
    if (!confirm('Deseja realmente excluir esta especialidade?')) return;

    try {
        await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
        await carregarEspecialidades();
        alert('Excluída com sucesso!');
    } catch (error) {
        console.error(error);
        alert('Erro ao excluir a especialidade!');
    }
}

function resetFormularioEspecialidade() {
    const form = document.getElementById('especialidadeForm');
    if (form) {
        form.reset();
    }
    document.getElementById('idEspecialidade').value = '';
}

function cancelarEdicaoEspecialidade() {
    especialidadeEmEdicaoId = null;

    document.getElementById('formTitle').textContent = 'Nova Especialidade';
    document.getElementById('btnSalvar').textContent = 'Salvar Especialidade';
    document.getElementById('btnCancelar').classList.add('hidden');

    resetFormularioEspecialidade();
}
