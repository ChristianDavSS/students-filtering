const faculty = document.getElementById("faculty");
const career = document.getElementById("career");
const generation = document.getElementById("generation");
const modality = document.getElementById("modality");
const graphic = document.getElementById("graphic");
let myChart = null;

const load = async () => {
    const charParams = {
        facultyId: faculty.value,
        careerId: career.value,
        generation: generation.value || null,
        modalityId: modality.value || null
    }

    const tableParams = {
        id: null,
        name: null,
        ...charParams
    }

    // Data to make a chart
    const {data: charData} = await axios.get("http://localhost:8080/student/chart", {
        params: charParams
    })

    const {data: tableData} = await axios.get("http://localhost:8080/student/filter", {
        params: tableParams
    })

    const bodyt = document.getElementById("bodyt");
    bodyt.innerHTML = '';
    tableData.forEach(e => {
        const tr = document.createElement("tr")
        tr.innerHTML = `
            <td>${e.id_number}</td>
            <td>${e.name}</td>
            <td>${e.generation}</td>
            <td>${e.faculty}</td>
            <td>${e.career}</td>
            <td>${e.modality}</td>
            <td>${e.teachers?.sinodal_presidente}</td>
            <td>${e.teachers?.sinodal_secretario}</td>
            <td>${e.teachers?.sinodal_vocal}</td>
            <td>${e.project || "No aplica"}</td>
            <td>${e.teachers?.asesor || "No aplica"}</td>
            <td>${e.teachers?.coasesor || "No aplica"}</td>
        `
        bodyt.appendChild(tr)
    })

    if (bodyt.innerHTML === ''){
        document.getElementById("no-table-msg").innerHTML = 'No se encontraron valores con los filtros establecidos'
        document.getElementById("table").style = "visibility: hidden;"
        graphic.style = "visibility: hidden;"
        return;
    } else {
        document.getElementById("no-table-msg").innerHTML = ''
        document.getElementById("table").style = "visibility: visible;"
        graphic.style = "visibility: visible;"
    }

    if (myChart){
        myChart.data.labels = Object.keys(charData);
        myChart.data.datasets[0].data = Object.values(charData);
        myChart.update();
    } else {
        myChart = new Chart(graphic.getContext('2d'), {
            type: 'pie',
            data: {
                labels: Object.keys(charData),
                datasets: [{
                    data: Object.values(charData),
                    label: '', // color del borde
                    borderWidth: 2
                }]
            }, options: {
                responsive: false,
                maintainAspectRatio: false
            }
        });
    }
}


const getFaculties = async () => {
    if (faculty.value != '') return;
    const { data } = await axios.get("http://localhost:8080/faculty")
    faculty.innerHTML = "";
    const opc = document.createElement("option")
    opc.value = ''
    opc.innerHTML = "Seleccionar..."
    faculty.appendChild(opc);
    data.forEach(e => {
        const opc = document.createElement("option")
        opc.value = e.id
        opc.innerHTML = e.name

        faculty.appendChild(opc)
    })
}


const getCareers = async (facultyId = null) => {
    const { data } = await axios.get("http://localhost:8080/career", {
        params: {
            facultyId: facultyId
        }
    })
    career.innerHTML = "";
    const opc = document.createElement("option")
    opc.value = ''
    opc.innerHTML = "Seleccionar..."
    career.appendChild(opc);
    data.forEach(e => {
        const opc = document.createElement("option")
        opc.value = e.id
        opc.innerHTML = e.name

        career.appendChild(opc)
    })
}

const getGenerations = async () => {
    if (generation.value != '') return;
    const { data } = await axios.get("http://localhost:8080/student/generations")
    generation.innerHTML = "";
    const opc = document.createElement("option")
    opc.value = ''
    opc.innerHTML = "Seleccionar..."
    generation.appendChild(opc);
    data.forEach(e => {
        const opc = document.createElement("option")
        opc.value = e
        opc.innerHTML = e

        generation.appendChild(opc)
    })
}

const getModalities = async () => {
    if (modality.value != '') return;
    const { data } = await axios.get("http://localhost:8080/modality")
    modality.innerHTML = '';
    const opc = document.createElement("option")
    opc.value = ''
    opc.innerHTML = "Seleccionar..."
    modality.appendChild(opc);
    data.forEach(e => {
        const opc = document.createElement("option")
        opc.value = e.id
        opc.innerHTML = e.name

        modality.appendChild(opc)
    })
}


window.addEventListener("DOMContentLoaded", async () => {
    await getFaculties();
    await getCareers();
    await getGenerations();
    await getModalities();
});


faculty.addEventListener("change", () => {
    getCareers(faculty.value)
    load();
})

career.addEventListener("change", () => {
    load();
})

generation.addEventListener("change", () => {
    load();
})

modality.addEventListener("change", () => {
    load();
})