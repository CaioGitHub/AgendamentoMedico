const API_MEDICOS = '/api/medicos';
const API_ESPECIALIDADES = '/api/especialidades';

let paginaAtual = 0;       // Spring usa base 0
const itensPorPagina = 5;  // deve ser igual ao size usado no back-end
let totalPaginas = 1;      // vem do back-end

document.addEventListener('DOMContentLoaded', () => {
    carregarEspecialidades();
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
});

// ======================= CARREGAR ESPECIALIDADES =======================
function carregarEspecialidades() {
    fetch(API_ESPECIALIDADES)
        .then(res => res.json())
        .then(data => {
            const select = document.getElementById('especialidades');
            select.innerHTML = '';
            data.forEach(esp => {
                const option = document.createElement('option');
                option.value = esp.id;
                option.textContent = esp.nome;
                select.appendChild(option);
            });
        })
        .catch(err => console.error('Erro ao carregar especialidades:', err));
}

// ======================= CARREGAR MÉDICOS =======================
function carregarMedicos() {
    fetch(`${API_MEDICOS}?page=${paginaAtual}&size=${itensPorPagina}`)
        .then(res => res.json())
        .then(data => {
            // A API retorna objeto com content e totalPages
            const medicos = data.content;
            totalPaginas = data.totalPages;

            renderizarMedicos(medicos);
        })
        .catch(err => console.error('Erro ao carregar médicos:', err));
}

// ======================= RENDERIZAR MÉDICOS =======================
function renderizarMedicos(medicos) {
    const container = document.getElementById('medicosList');
    const pageInfo = document.getElementById('pageInfo');
    const prevBtn = document.getElementById('prevPage');
    const nextBtn = document.getElementById('nextPage');

    container.innerHTML = '';

    if (!medicos || medicos.length === 0) {
        container.innerHTML = '<p>Nenhum médico cadastrado.</p>';
        pageInfo.textContent = `Página ${paginaAtual + 1} de ${totalPaginas}`;
        prevBtn.disabled = paginaAtual === 0;
        nextBtn.disabled = paginaAtual >= totalPaginas - 1;
        return;
    }

    // renderiza os cards
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

    // atualiza info de página
    pageInfo.textContent = `Página ${paginaAtual + 1} de ${totalPaginas}`;
    prevBtn.disabled = paginaAtual === 0;
    nextBtn.disabled = paginaAtual >= totalPaginas - 1;
}

// ======================= SALVAR MÉDICO =======================
function salvarMedico(event) {
    event.preventDefault();

    const nome = document.getElementById('nome').value.trim();
    const crm = document.getElementById('crm').value.trim();
    const endereco = document.getElementById('endereco').value.trim();
    const especialidades = Array.from(document.getElementById('especialidades').selectedOptions).map(opt => parseInt(opt.value));

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
            paginaAtual = 0; // volta para primeira página
            carregarMedicos();
        })
        .catch(err => alert(err.message));
}
