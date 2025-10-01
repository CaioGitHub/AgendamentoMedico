const API_URL = '/api/convenios';
let currentPage = 1;
const pageSize = 5;
let convenios = [];

document.addEventListener('DOMContentLoaded', () => {
    carregarConvenios();

    document.getElementById('btnSalvar').addEventListener('click', salvarConvenio);
    document.getElementById('prevPage').addEventListener('click', () => mudarPagina(-1));
    document.getElementById('nextPage').addEventListener('click', () => mudarPagina(1));

    const telefoneInput = document.getElementById('telefoneConvenio');
    telefoneInput.addEventListener('input', aplicarMascaraTelefone);
});

function aplicarMascaraTelefone(event) {
    let valor = event.target.value.replace(/\D/g, ''); // remove tudo que não é número

    if (valor.length > 11) valor = valor.slice(0, 11); // limita a 11 dígitos (DDD + 9 números)

    if (valor.length <= 10) {
        // Formato: (00)0000-0000
        valor = valor.replace(/^(\d{2})(\d{4})(\d{0,4})$/, '($1)$2-$3');
    } else {
        // Formato: (00)00000-0000
        valor = valor.replace(/^(\d{2})(\d{5})(\d{0,4})$/, '($1)$2-$3');
    }

    event.target.value = valor;
}

async function carregarConvenios() {
    try {
        const response = await fetch(API_URL);
        convenios = await response.json();
        renderizarLista();
    } catch (error) {
        console.error(error);
        document.getElementById('listaConvenios').innerHTML = '<li>Erro ao carregar convênios</li>';
    }
}

function renderizarLista() {
    const lista = document.getElementById('listaConvenios');
    lista.innerHTML = '';

    const start = (currentPage - 1) * pageSize;
    const end = start + pageSize;
    const pagina = convenios.slice(start, end);

    if (pagina.length === 0) {
        lista.innerHTML = '<li>Nenhum convênio cadastrado.</li>';
    } else {
        pagina.forEach(c => {
            const li = document.createElement('li');
            li.innerHTML = `
                <span>${c.id} - ${c.nome} (${c.cobertura})</span>
                <div>
                    <button class="btn-action" onclick="editar(${c.id}, '${c.nome}', '${c.cobertura}', '${c.telefoneContato || ''}')">Editar</button>
                    <button class="btn-action" onclick="deletarConvenio(${c.id})">Excluir</button>
                </div>
            `;
            lista.appendChild(li);
        });
    }

    atualizarPaginacao();
}

function atualizarPaginacao() {
    const totalPaginas = Math.ceil(convenios.length / pageSize);
    document.getElementById('pageInfo').textContent = `Página ${currentPage} de ${totalPaginas || 1}`;

    document.getElementById('prevPage').disabled = currentPage === 1;
    document.getElementById('nextPage').disabled = currentPage === totalPaginas || totalPaginas === 0;
}

function mudarPagina(delta) {
    currentPage += delta;
    renderizarLista();
}

async function salvarConvenio() {
    const id = document.getElementById('idConvenio').value;
    const nome = document.getElementById('nomeConvenio').value.trim();
    const cobertura = document.getElementById('coberturaConvenio').value.trim();
    let telefoneContato = document.getElementById('telefoneConvenio').value.replace(/\D/g, '');

    if (!nome || !cobertura) {
        alert('Preencha todos os campos obrigatórios!');
        return;
    }

    const convenio = { nome, cobertura, telefoneContato };

    try {
        if (id) {
            await fetch(`${API_URL}/${id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(convenio)
            });
        } else {
            await fetch(API_URL, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(convenio)
            });
        }

        limparFormulario();
        await carregarConvenios();
        alert('Convênio salvo com sucesso!');

    } catch (error) {
        console.error(error);
        alert('Erro ao salvar o convênio!');
    }
}

function editar(id, nome, cobertura, telefoneContato) {
    document.getElementById('idConvenio').value = id;
    document.getElementById('nomeConvenio').value = nome;
    document.getElementById('coberturaConvenio').value = cobertura;
    document.getElementById('telefoneConvenio').value = telefoneContato;
}

function limparFormulario() {
    document.getElementById('idConvenio').value = '';
    document.getElementById('nomeConvenio').value = '';
    document.getElementById('coberturaConvenio').value = '';
    document.getElementById('telefoneConvenio').value = '';
}

async function deletarConvenio(id) {
    if (!confirm('Deseja realmente excluir este convênio?')) return;

    try {
        await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
        await carregarConvenios();
        alert('Convênio excluído com sucesso!');
    } catch (error) {
        console.error(error);
        alert('Erro ao excluir o convênio!');
    }
}
