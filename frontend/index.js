// Elementos del DOM para búsqueda tabular
const searchInput = document.getElementById('searchInput');
const filterSelect = document.getElementById('filterSelect');
const searchBtn = document.getElementById('searchBtn');
const tableBody = document.getElementById('tableBody');
const loading = document.getElementById('loading');
const noResults = document.getElementById('noResults');

// Elementos del DOM para búsqueda gráfica
const facultySelectChart = document.getElementById("facultySelectChart")
const careerSelectChart = document.getElementById("careerSelectChart")
const generationSelectChart = document.getElementById("generationSelectChart")
const modalitySelectChart = document.getElementById("modalitySelectChart")
const selectsChartList = [facultySelectChart, careerSelectChart, generationSelectChart, modalitySelectChart]
const searchBtnChart = document.getElementById('searchBtnChart');
const clearBtnChart = document.getElementById('clearBtnChart');
const chartsSection = document.getElementById('chartsSection');
const chartTitle = document.getElementById('chartTitle');
const loadingChart = document.getElementById('loadingChart');
const chartStats = document.getElementById('chartStats');
const chartWrapper = document.getElementById('chartWrapper');
const noChartData = document.getElementById('noChartData');
const chartTypeSelector = document.getElementById('chartTypeSelector');
const totalTitulados = document.getElementById('totalTitulados');
const categoriasMostradas = document.getElementById('categoriasMostradas');

// Elementos del DOM para el modal de registro
const registerBtn = document.getElementById('registerBtn');
const registerModal = document.getElementById('registerModal');
const closeBtn = document.querySelector('.close-btn');
const registerForm = document.getElementById('registerForm');
const regFacultad = document.getElementById('regFaculty');
const regCarrera = document.getElementById('regCareer');
const regNcta = document.getElementById('regNcta');
const regNombre = document.getElementById('regNombre');
const regGeneracion = document.getElementById('regGeneration');
const regFechaTitulacion = document.getElementById('regFechaTitulacion');
const regModalidad = document.getElementById('regModality');
const regProyecto = document.getElementById('regProyecto');
const regAsesor = document.getElementById('regAsesor');
const regCoasesor = document.getElementById('regCoasesor');
const regSinodal1 = document.getElementById('regSinodalPresidente');
const regSinodal2 = document.getElementById('regSinodalSecretario');
const regSinodal3 = document.getElementById('regSinodalVocal');
const modalidadesConAsesor = [
    "Tesis", 
    "Tesina", 
    "Articulo de investigación", 
    "Sistematización de experiencia profesional"
];

// Campos de asesor/coasesor para mostrar/ocultar
const asesorField = document.getElementById('asesorField');
const coasesorField = document.getElementById('coasesorField');
const proyectoField = document.getElementById('proyectoField');

// Variables globales
let currentChart = null;
let currentChartType = 'bar';
// Para guardar los datos de las gráficas al cambiar el tipo
let labels, values;

// Paleta de colores minimalista
const colors = {
    primary: ['#667eea', '#764ba2', '#f093fb', '#f5576c', '#4facfe', '#00f2fe', '#a8edea', '#fed6e3'],
    backgrounds: [
        'rgba(102, 126, 234, 0.8)',
        'rgba(118, 75, 162, 0.8)', 
        'rgba(240, 147, 251, 0.8)',
        'rgba(245, 87, 108, 0.8)',
        'rgba(79, 172, 254, 0.8)',
        'rgba(0, 242, 254, 0.8)',
        'rgba(168, 237, 234, 0.8)',
        'rgba(254, 214, 227, 0.8)'
    ]
};

// Función para mostrar los datos en la tabla
function displayResults(data) {
    tableBody.innerHTML = '';
    
    if (data.length === 0) {
        noResults.style.display = 'block';
        return;
    }
    
    noResults.style.display = 'none';
    
    data.forEach(item => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${item.id_number}</td>
            <td>${item.name}</td>
            <td>${item.faculty}</td>
            <td>${item.career}</td>
            <td>${item.generation}</td>
            <td><span class="status-badge status-titulado">${item.modality}</span></td>
            <td>${item.date}</td>
            <td>${item.teachers?.sinodal_presidente}</td>
            <td>${item.teachers?.sinodal_secretario}</td>
            <td>${item.teachers?.sinodal_vocal}</td>
            <td>${item.project || "N/A"}</td>
            <td>${item.teachers?.asesor || "N/A"}</td>
            <td>${item.teachers?.coasesor || "N/A"}</td>
        `;
        tableBody.appendChild(row);
    });
}

// Función de búsqueda tabular
async function performSearch(extraParams = {}) {
    const query = searchInput.value.toLowerCase().trim();
    const filterType = filterSelect.value;
    
    // Mostrar indicador de carga
    loading.style.display = 'block';
    tableBody.innerHTML = '';
    noResults.style.display = 'none';
    
    // Simular delay de API
    loading.style.display = 'none';

    // Objeto para los parametros de la búsqueda
    const queryParams = {
        id: null,
        name: null,
        ...extraParams
    }

    // Obtener los datos del backend
    filterType === "ncta" ? queryParams["id"] = query : queryParams["name"] = query
    const { data: filteredData } = await axios.get("http://localhost:8080/student/filter", {
        params: queryParams
    })
    
    displayResults(filteredData);
    if (Object.keys(extraParams)[0] === 'isTrusted') clearSearch();
}

// Función para obtener datos del backend y settearlos en selectbox.
const getSelectData = async (url, params = {}, select) => {
    select.innerHTML = '';
    createDefaultOpcs(select);

    const { data } = await axios.get(`http://localhost:8080/${url}`, {
        params: params
    });

    data.forEach(e => {
        const opc = document.createElement("option");
        opc.value = !url.includes("generation") ? e.id : e
        opc.innerHTML = !url.includes("generation") ? e.name : e
        select.appendChild(opc);
    });
}

const createDefaultOpcs = (select) => {
    const opc = document.createElement("option");
    opc.value = "";
    opc.innerHTML = "Selecciona..."
    select.appendChild(opc);
}

// FUNCIÓN CORREGIDA: performChartSearch()
async function performChartSearch() {
    const facultyValue = facultySelectChart.value;
    const careerValue = careerSelectChart.value;
    const generationValue = generationSelectChart.value;
    const modalityValue = modalitySelectChart.value;

    showLoading();
    
    const params = {
        facultyId: facultyValue || null,
        careerId: careerValue || null,
        generation: generationValue || null,
        modalityId: modalityValue || null
    }
    const { data: chartData } = await axios.get("http://localhost:8080/student/chart", {
        params: params
    })

    if (Object.values(chartData).filter(v => v === 0).length === chartData.length) {
        loadingChart.style.display = 'none';
        noChartData.innerHTML = `
            <div style="font-size: 3em; opacity: 0.5; margin-bottom: 15px;">🔍</div>
            <p><strong>No se encontraron datos</strong></p>
            <p>Intenta con otras opciones.</p>
        `;
        noChartData.style.display = 'block';
    }

    processAndDisplayChart(chartData);
    performSearch(params);
    
    chartsSection.scrollIntoView({ behavior: 'smooth' });
}

// Función para mostrar loading
function showLoading() {
    loadingChart.style.display = 'block';
    chartStats.style.display = 'none';
    chartWrapper.style.display = 'none';
    noChartData.style.display = 'none';
    chartTypeSelector.style.display = 'none';
}

// Función para procesar y mostrar gráfico
function processAndDisplayChart(data) {
    loadingChart.style.display = 'none';
    
    if (data.length === 0) {
        noChartData.innerHTML = `
            <div style="font-size: 3em; opacity: 0.5; margin-bottom: 15px;">🔍</div>
            <p><strong>No se encontraron datos</strong></p>
            <p>Intenta con otros términos de búsqueda</p>
        `;
        noChartData.style.display = 'block';
        return;
    }
    
    labels = Object.keys(data);
    values = Object.values(data);
    
    if (labels.length === 0) {
        noChartData.innerHTML = `
            <div style="font-size: 3em; opacity: 0.5; margin-bottom: 15px;">📊</div>
            <p><strong>Sin datos para mostrar</strong></p>
            <p>No hay información disponible para estos filtros.</p>
        `;
        noChartData.style.display = 'block';
        return;
    }
    
    // Mostrar estadísticas
    updateStats(Object.values(data).reduce((i, n) => i + n, 0), labels.length);
    
    // Mostrar elementos
    chartStats.style.display = 'flex';
    chartWrapper.style.display = 'block';
    chartTypeSelector.style.display = 'flex';
    noChartData.style.display = 'none';
    
    // Generar gráfico
    generateChart(currentChartType);
}

// Función para actualizar estadísticas
function updateStats(total, categories) {
    totalTitulados.textContent = total;
    categoriasMostradas.textContent = categories;
}

// Función para generar gráfico
function generateChart(chartType) {
    const canvas = document.getElementById('chartCanvas');
    if (!canvas) return;
    
    const ctx = canvas.getContext('2d');
    
    // Destruir gráfico anterior
    if (currentChart) {
        currentChart.destroy();
    }
    
    let titleText = `Distribución gráfica`;
    chartTitle.textContent = titleText;
    
    // Configuración del gráfico según el tipo
    let chartConfig = {
        type: chartType,
        data: {
            labels: labels,
            datasets: [{
                label: 'Cantidad',
                data: values,
                backgroundColor: colors.backgrounds.slice(0, labels.length),
                borderColor: colors.primary.slice(0, labels.length),
                borderWidth: 2
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: chartType === 'doughnut',
                    position: 'bottom',
                    labels: {
                        padding: 20,
                        usePointStyle: true,
                        font: {
                            size: 12
                        }
                    }
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            const total = values.reduce((a, b) => a + b, 0);
                            const percentage = ((context.parsed / total) * 100).toFixed(1);
                            const value = chartType === 'doughnut' ? context.parsed : context.parsed.y;
                            return `${context.label}: ${value} (${percentage}%)`;
                        }
                    }
                },
                title: {
                    display: true,
                    text: titleText,
                    font: {
                        size: 16,
                        weight: 'bold'
                    }
                }
            },
            animation: {
                duration: 1000,
                easing: 'easeOutQuart'
            }
        }
    };
    
    // Configuraciones específicas por tipo de gráfico
    if (chartType === 'bar') {
        chartConfig.options.scales = {
            y: {
                beginAtZero: true,
                ticks: {
                    stepSize: 1,
                    color: '#6c757d'
                },
                grid: {
                    color: 'rgba(0,0,0,0.1)'
                }
            },
            x: {
                ticks: {
                    color: '#6c757d',
                    maxRotation: 45,
                    minRotation: 0
                },
                grid: {
                    display: false
                }
            }
        };
        chartConfig.data.datasets[0].borderRadius = 8;
        chartConfig.data.datasets[0].borderSkipped = false;
    } else if (chartType === 'line') {
        chartConfig.data.datasets[0].fill = false;
        chartConfig.data.datasets[0].tension = 0.4;
        chartConfig.data.datasets[0].pointBackgroundColor = colors.primary[0];
        chartConfig.data.datasets[0].pointBorderColor = '#fff';
        chartConfig.data.datasets[0].pointBorderWidth = 2;
        chartConfig.data.datasets[0].pointRadius = 6;
        chartConfig.data.datasets[0].backgroundColor = colors.backgrounds[0];
        chartConfig.data.datasets[0].borderColor = colors.primary[0];
        
        chartConfig.options.scales = {
            y: {
                beginAtZero: true,
                ticks: {
                    stepSize: 1,
                    color: '#6c757d'
                },
                grid: {
                    color: 'rgba(0,0,0,0.1)'
                }
            },
            x: {
                ticks: {
                    color: '#6c757d'
                },
                grid: {
                    color: 'rgba(0,0,0,0.1)'
                }
            }
        };
    } else if (chartType === 'doughnut') {
        chartConfig.data.datasets[0].cutout = '60%';
        chartConfig.data.datasets[0].borderWidth = 0;
    }
    
    // Crear gráfico
    currentChart = new Chart(ctx, chartConfig);
}

// Función para limpiar búsqueda y gráficos
function clearSearch() {
    // Resetear a valores por defecto
    selectsChartList.forEach(e => {
        e.selectedIndex = 0;
    });
    currentChartType = 'bar';
    
    // Actualizar botón activo
    document.querySelectorAll('.chart-type-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    document.querySelector('.chart-type-btn[data-type="bar"]').classList.add('active');
    
    // Ocultar gráficos y mostrar mensaje inicial
    chartStats.style.display = 'none';
    chartWrapper.style.display = 'none';
    chartTypeSelector.style.display = 'none';
    noChartData.innerHTML = `
        <div style="font-size: 3em; opacity: 0.5; margin-bottom: 15px;">📊</div>
        <p><strong>¡Genera tu primer gráfico!</strong></p>
        <p>Selecciona una categoría y una opción para ver las estadísticas</p>
    `;
    noChartData.style.display = 'block';
    
    // Destruir gráfico actual
    if (currentChart) {
        currentChart.destroy();
        currentChart = null;
    }
}

// --- Lógica del formulario de registro ---
registerBtn.addEventListener('click', () => {
    registerModal.style.display = 'block';
    // Asegurarse de que los campos de asesor/coasesor estén visibles al abrir el modal
    asesorField.classList.remove('hidden');
    coasesorField.classList.remove('hidden');
    regAsesor.setAttribute('required', 'true');
    regCoasesor.setAttribute('required', 'true');
});

closeBtn.addEventListener('click', () => {
    registerModal.style.display = 'none';
});

window.addEventListener('click', (event) => {
    if (event.target == registerModal) {
        registerModal.style.display = 'none';
    }
});

// Lógica para mostrar/ocultar campos de asesor
regModalidad.addEventListener('change', () => {
    const selectedModalidad = regModalidad.options[regModalidad.selectedIndex].text;

    if (modalidadesConAsesor.includes(selectedModalidad)) {
        asesorField.classList.remove('hidden');
        coasesorField.classList.remove('hidden');
        coasesorField.classList.remove('hidden');
        proyectoField.classList.remove('hidden');
        regAsesor.setAttribute('required', 'true');
        regCoasesor.setAttribute('required', 'true');
        regProyecto.setAttribute('required', 'true');
    } else {
        asesorField.classList.add('hidden');
        coasesorField.classList.add('hidden');
        proyectoField.classList.add('hidden');
        regAsesor.removeAttribute('required');
        regCoasesor.removeAttribute('required');
        regProyecto.removeAttribute('required')
        regAsesor.value = "";
        regCoasesor.value = "";
        regProyecto.value = "";
    }
});

registerForm.addEventListener('submit', (event) => {
    event.preventDefault();

    const registerStudent = async () => {
        const params = {
            id_number: regNcta.value,
            name: regNombre.value,
            facultyId: regFacultad.value,
            careerId: regCarrera.value,
            generation: regGeneracion.value,
            date: regFechaTitulacion.value,
            modalityId: regModalidad.value,
            project: regProyecto.value || null,
            teachers: {
                asesor: regAsesor.value || null,
                coasesor: regCoasesor.value || null,
                sinodalpresidente: regSinodal1.value,
                sinodalsecretario: regSinodal2.value,
                sinodalvocal: regSinodal3.value,
            }
        };

        try{
            await axios.post("http://localhost:8080/student", params)
        } catch (e) {
            if (e.response.status === 409) alert("Este usuario ya está registrado.")
        }
    }

    registerStudent();

    alert('¡Titulado registrado con éxito!');
    registerModal.style.display = 'none';
    registerForm.reset();
    // Actualizar la tabla
    performSearch();
});

// Event listener para las carreras dependientes de una facultad
facultySelectChart.addEventListener("change", () => {
    getSelectData("career", {facultyId: facultySelectChart.value}, careerSelectChart);
});

// Event listener para el filtro dinámico de facultades/carreras en el modal de registro
regFacultad.addEventListener('change', () => {
    getSelectData("career", {facultyId: regFacultad.value}, regCarrera);
    regCarrera.disabled = regFacultad.value === "";
});

// Event Listeners - SIN DUPLICACIÓN
document.addEventListener('DOMContentLoaded', () => {
    // Función para obtener los datos iniciales de la tabla.
    const getTeachers = async () => {
        const selectsTeachers = [regAsesor, regCoasesor, regSinodal1, regSinodal2, regSinodal3];
        selectsTeachers.forEach(e => {
            getSelectData("teachers", {}, e);
        });
    }
    getTeachers();

    // Función para obtener las modalidades y agruparlas (REGISTRO DE ESTUDIANTE)
    const getModality = async () => {
        const { data } = await axios.get("http://localhost:8080/modality");
        data.forEach(e => {
            const opc = document.createElement("option");
            opc.value = e.id
            opc.innerHTML = e.name

            if (modalidadesConAsesor.includes(e.name)) {
                document.getElementById("conAsesores").appendChild(opc)
            } else {
                document.getElementById("sinAsesores").appendChild(opc)
            }
        })
    }
    // Agrupar las opciones de modalidad en sus grupos
    getModality();

    searchBtn.addEventListener('click', performSearch);
    searchInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            performSearch();
            clearSearch();
        }
    });

    // Event listeners para búsqueda gráfica
    searchBtnChart.addEventListener('click', () => {
        performChartSearch();
        searchInput.value = '';
    });
    clearBtnChart.addEventListener('click', () => {
        clearSearch();
        performSearch();
    });

    // Event listeners para botones de tipo de gráfico
    document.querySelectorAll('.chart-type-btn').forEach(btn => {
        btn.addEventListener('click', function() {            
            // Actualizar botón activo
            document.querySelectorAll('.chart-type-btn').forEach(b => b.classList.remove('active'));
            this.classList.add('active');
            
            // Cambiar tipo de gráfico
            currentChartType = this.dataset.type;
            generateChart(currentChartType);
        });
    });

    selectsChartList.forEach(e => {
        let url = e.id.split("S")[0];

        if (url.includes("generation")) url = "student/" + url
        getSelectData(url, {}, e)
    });

    const aux = [regFacultad, regCarrera];
    aux.forEach(e => {
        getSelectData(e.id.split("g")[1].toLowerCase(), {}, e)
    });

    performSearch();
});