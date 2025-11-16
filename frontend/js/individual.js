/*
    INDIVIDUAL SECTION
*/
import { api } from './api.js'
import { fill_table_data } from './index.js';

const studentResults = document.querySelector(".individual-results");
const tableBody = document.querySelectorAll(".general-table-body");

const studentIdInput = document.getElementById("studentId-filter-input");
const studentNameInput = document.getElementById("student-name-input");
const studentIdBtn = document.getElementById("studentId-search");
const studentNameBtn = document.getElementById("name-search");
const studentSearchBtn = document.querySelectorAll(".student-search-btn");

const performSearch = async () => {
    // If both inputs are null we return. We force one of them to have content
    if (!studentIdInput.value && !studentNameInput.value) return;

    const obj = {
        studentId: studentIdInput.value || null,
        name: studentNameInput.value || null
    };

    const { data } = await api.get("/student", {params: obj})
    if (data.length < 1) {
        studentResults.classList.remove("active");
        tableBody[0].innerHTML = '';
        return;
    }

    studentResults.classList.add("active");
    fill_table_data(data, 0);
}

studentIdBtn.addEventListener("click", () => {
    studentNameInput.value = '';
})

studentNameBtn.addEventListener("click", () => {
    studentIdInput.value = '';
})

studentSearchBtn.forEach(e => {
    e.addEventListener("click", () => {
        performSearch();
    });
});