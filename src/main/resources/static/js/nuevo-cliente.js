document.addEventListener("DOMContentLoaded", () => {

    const dniInput =
        document.getElementById("dni");

    const form =
        document.getElementById("clienteForm");

    let clienteExiste = false;

    dniInput.addEventListener("blur", async () => {

        const dni = dniInput.value.trim();

        if(dni === "") return;

        try {

            const response =
                await fetch(`/clientes/existe-dni?dni=${dni}`);

            const clienteId =
                await response.json();

            if(clienteId) {

                clienteExiste = true;

                document
                    .getElementById("modalClienteExiste")
                    .classList.remove("hidden");

                document
                    .getElementById("verFichaBtn")
                    .href = `/clientes/${clienteId}`;

            } else {

                clienteExiste = false;
            }

        } catch(error) {

            console.error(error);
        }
    });

    form.addEventListener("submit", (e) => {

        if(clienteExiste) {

            e.preventDefault();
        }
    });

});

function cerrarModal() {

    document
        .getElementById("modalClienteExiste")
        .classList.add("hidden");
}