import { api } from './api.js';

/*
    index.js: File for the general filters
*/

// Variables globales
const tableBody = document.querySelectorAll(".general-table-body");
// SELECTS
const facultySelect = document.getElementById("faculty-select");
const careerSelect = document.getElementById("career-select");
const generationSelect = document.getElementById("generation-select");
const modalitySelect = document.getElementById("modality-select");
const canvas = document.getElementById("chart-canvas");
let currChart = null;

// Paleta de colores 
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


const selects = [facultySelect, careerSelect, generationSelect, modalitySelect]

// Functions

// Funciones para el llenado de los selectbox

const fetchChartData = async () => {
    const obj = {
        facultyId: document.getElementById("faculty-select").value || null,
        careerId: document.getElementById("career-select").value || null,
        generation: document.getElementById("generation-select").value || null,
        modalityId: document.getElementById("modality-select").value || null
    };
    const { data } = await api.get("student/chart", {params: obj});
    generateChart(data);
};

const generateChart = (data) => {
    if (!canvas) return;

    const ctx = canvas.getContext("2d");

    if (currChart != null) {
        currChart.destroy();
    }

    const labels = Object.values(data);

    const config = {
        type: "bar",
        data: {
            labels: Object.keys(data),
            datasets: [{
                label: "Cantidad de alumnos",
                data: labels,
                backgroundColor: colors.backgrounds.slice(0, labels.length),
                borderColor: colors.primary.slice(0, labels.length),
                borderWidth: 2
            }]
        },
        options: {
            responsive: true
        }
    }

    currChart = new Chart(ctx, config);
}

const fill_selects = async () => {
    const { data } = await api.get("/filters")
    const obj = {
        "faculty": facultySelect,
        "career": careerSelect,
        "generation": generationSelect,
        "modality": modalitySelect
    };

    Object.keys(obj).forEach(e => {
        fill(data[e], obj[e])
    })
};

const fill = (data, select) => {
    /*
        Fills the selects with the data
    */
    select.innerHTML = '';
    const opc = document.createElement("option");
    opc.innerHTML = "Todas";
    opc.value = "";
    select.appendChild(opc);
    data.forEach(e => {
        const opc = document.createElement("option");
        opc.value = e.id || e.name;
        opc.innerHTML = e.name;

        select.appendChild(opc);
    });
}

// Funciones de la tabla principal
const fill_table = async () => {
    /*
        Method used to retrieve data from the database
    */
   // Query params
    const obj = {
        facultyId: document.getElementById("faculty-select").value || null,
        careerId: document.getElementById("career-select").value || null,
        generation: document.getElementById("generation-select").value || null,
        modalityId: document.getElementById("modality-select").value || null
    };

    // Fetch the data from the API
    const { data } = await api.get("/student", {params: obj});

    fill_table_data(data, 1);
};

export const fill_table_data = (data, idx) => {
    // Clear the body before the next query
    tableBody[idx].innerHTML = '';

    data.forEach(e => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${e.studentId}</td>
            <td>${e.name}</td>
            <td>${e.faculty}</td>
            <td>${e.career}</td>
            <td>${e.generation}</td>
            <td>${e.modality}</td>
            <td>${e.date}</td>
            <td>${e.teachers?.SINODAL_PRESIDENTE}</td>
            <td>${e.teachers?.SINODAL_SECRETARIO}</td>
            <td>${e.teachers?.SINODAL_VOCAL}</td>
            <td>${e.project || "No aplica"}</td>
            <td>${e.teachers?.ASESOR || "No aplica"}</td>
            <td>${e.teachers?.COASESOR || "No aplica"}</td>
        `;
        tableBody[idx].appendChild(row);
    })
}

// Listeners
document.getElementById("general-filter-btn").addEventListener("click", () => {
    fill_selects();
    fill_table();
    fetchChartData();
})

document.addEventListener("DOMContentLoaded", async () => {
    fill_selects();
    fill_table();
    fetchChartData();
});

selects.forEach(e => {
    e.addEventListener("change", () => {
        fill_table();
        fetchChartData();
    })
});

document.getElementById("reset-filters").addEventListener("click", () => {
    selects.forEach(s => {
        s.selectedIndex = 0;
    });
    fill_table();
    fetchChartData();
});