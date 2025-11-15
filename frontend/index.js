// Global variables
const tabs = document.querySelectorAll(".tab")
const individual_tabs = document.querySelectorAll(".ind-tab");
const management_tabs = document.querySelectorAll(".management-tab");

// Dynamic general section selection
document.querySelectorAll(".section-btn").forEach((btn, i) => {
    btn.addEventListener("click", () => {
        tabs.forEach(t => t.classList.remove("active"));
        tabs[i].classList.add("active");
    });
});

// Dynamic selection of the individual filter section
document.querySelectorAll(".individual-btn").forEach((btn, i) => {
    btn.addEventListener("click", () => {
        individual_tabs.forEach(t => t.classList.remove("active"));
        individual_tabs[i].classList.add("active");
    });
});

// Dynamic selection of the management
document.querySelectorAll(".management-btn").forEach((btn, i) => {
    btn.addEventListener("click", () => {
        management_tabs.forEach(t => t.classList.remove("active"));
        management_tabs[i].classList.add("active");
    });
});