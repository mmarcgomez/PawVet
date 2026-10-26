const razaInput =
    document.getElementById("razaInput");

const especieSelect =
    document.getElementById("especieSelect");

const suggestions =
    document.getElementById("razasSuggestions");

razaInput.addEventListener("input",
    async function() {

    const especie =
        especieSelect.value;

    const query =
        razaInput.value;

    suggestions.innerHTML = "";

    // OTRO = texto libre

    if(especie === "OTRO") {
        return;
    }

    if(query.length < 2) {
        return;
    }

    // CANINO o FELINO

    const response =
        await fetch(
            `/mascotas/razas?q=${query}&especie=${especie}`
        );

    const razas =
        await response.json();

    razas.forEach(raza => {

        const div =
            document.createElement("div");

        div.classList.add("suggestion-item");

        div.innerText = raza;

        div.onclick = () => {

            razaInput.value = raza;

            suggestions.innerHTML = "";
        };

        suggestions.appendChild(div);
    });
});