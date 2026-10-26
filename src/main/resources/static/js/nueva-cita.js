const botonesHora = document.querySelectorAll(".hora");
const inputHora = document.getElementById("horaSeleccionada");

botonesHora.forEach(boton => {

    boton.addEventListener("click", () => {

        // quitar selección previa
        botonesHora.forEach(b =>
            b.classList.remove("seleccionada")
        );

        // marcar botón actual
        boton.classList.add("seleccionada");

        // guardar hora
        inputHora.value = boton.textContent.trim();
    });

});
const fechaInput =
    document.getElementById("fechaInput");

if(fechaInput){

    fechaInput.addEventListener("change", () => {

        const fecha =
            fechaInput.value;

        const mascotaId =
            document.querySelector(
                "input[name='mascotaId']"
            ).value;

        window.location.href =
            "/citas/nueva/" +
            mascotaId +
            "?fecha=" +
            fecha;
    });
}