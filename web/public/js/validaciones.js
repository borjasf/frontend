/**
 * Archivo de validaciones para formularios
 * Proporciona funciones reutilizables para validar formularios en cliente
 */

/**
 * Valida que un email sea válido
 * @param {string} email - Email a validar
 * @returns {boolean} true si es válido
 */
function validarEmail(email) {
    const regexEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regexEmail.test(email);
}

/**
 * Valida que un número sea positivo
 * @param {number} numero - Número a validar
 * @returns {boolean} true si es > 0
 */
function validarNumeroPositivo(numero) {
    return parseFloat(numero) > 0;
}

/**
 * Valida que dos campos coincidan (ej: contraseñas)
 * @param {string} campo1 - Valor del primer campo
 * @param {string} campo2 - Valor del segundo campo
 * @returns {boolean} true si coinciden
 */
function validarCoincidencia(campo1, campo2) {
    return campo1 === campo2;
}

/**
 * Valida que un campo no esté vacío
 * @param {string} valor - Valor del campo
 * @returns {boolean} true si no está vacío
 */
function validarNoVacio(valor) {
    return valor && valor.trim().length > 0;
}

/**
 * Muestra un mensaje de error en un campo
 * @param {HTMLElement} campo - Campo input
 * @param {string} mensaje - Mensaje de error
 */
function mostrarError(campo, mensaje) {
    // Si el campo está dentro de un wrapper, el mensaje lo insertamos DESPUÉS del wrapper
    const wrapper = campo.closest('.input-group') || campo.closest('.password-wrapper');
    const referencia = wrapper || campo;

    // Limpiar error previo
    const errorExistente = referencia.nextElementSibling;
    if (errorExistente && errorExistente.classList.contains('invalid-feedback')) {
        errorExistente.remove();
    }

    // Agregar clase de error
    campo.classList.add('is-invalid');

    // Crear mensaje de error
    const divError = document.createElement('div');
    divError.className = 'invalid-feedback d-block';
    divError.textContent = mensaje;
    divError.style.fontSize = '0.875rem';
    divError.style.color = '#dc3545';
    divError.style.marginTop = '0.25rem';

    referencia.parentNode.insertBefore(divError, referencia.nextSibling);
}

/**
 * Limpia los errores de un campo
 * @param {HTMLElement} campo - Campo input
 */
function limpiarError(campo) {
    campo.classList.remove('is-invalid');
    const wrapper = campo.closest('.input-group') || campo.closest('.password-wrapper');
    const referencia = wrapper || campo;
    const errorExistente = referencia.nextElementSibling;
    if (errorExistente && errorExistente.classList.contains('invalid-feedback')) {
        errorExistente.remove();
    }
}

/**
 * Limpia todos los errores de un formulario
 * @param {HTMLElement} formulario - Elemento form
 */
function limpiarErrorsFormulario(formulario) {
    const campos = formulario.querySelectorAll('input, textarea, select');
    campos.forEach(campo => limpiarError(campo));
}

/**
 * Valida un formulario de login
 * @param {HTMLElement} formulario - Elemento form
 * @returns {boolean} true si es válido
 */
function validarFormularioLogin(formulario) {
    limpiarErrorsFormulario(formulario);
    let esValido = true;

    const email = formulario.querySelector('input[name="email"]');
    const password = formulario.querySelector('input[name="password"]');

    // Validar email
    if (!email.value.trim()) {
        mostrarError(email, 'El email es requerido');
        esValido = false;
    } else if (!validarEmail(email.value)) {
        mostrarError(email, 'Ingresa un email válido (ej: usuario@ejemplo.com)');
        esValido = false;
    }

    // Validar contraseña
    if (!password.value) {
        mostrarError(password, 'La contraseña es requerida');
        esValido = false;
    }

    return esValido;
}

/**
 * Valida un formulario de registro
 * @param {HTMLElement} formulario - Elemento form
 * @returns {boolean} true si es válido
 */
function validarFormularioRegistro(formulario) {
    limpiarErrorsFormulario(formulario);
    let esValido = true;

    const nombre = formulario.querySelector('input[name="nombre"]');
    const apellidos = formulario.querySelector('input[name="apellidos"]');
    const email = formulario.querySelector('input[name="email"]');
    const password = formulario.querySelector('input[name="password"]');
    const confirmPassword = formulario.querySelector('input[name="confirmPassword"]');
    const fecha = formulario.querySelector('input[name="fechaNacimiento"]');

    // Validar nombre
    if (!validarNoVacio(nombre.value)) {
        mostrarError(nombre, 'El nombre es requerido');
        esValido = false;
    }

    // Validar apellidos
    if (!validarNoVacio(apellidos.value)) {
        mostrarError(apellidos, 'Los apellidos son requeridos');
        esValido = false;
    }

    // Validar email
    if (!email.value.trim()) {
        mostrarError(email, 'El email es requerido');
        esValido = false;
    } else if (!validarEmail(email.value)) {
        mostrarError(email, 'Ingresa un email válido (ej: usuario@ejemplo.com)');
        esValido = false;
    }

    // Validar contraseña
    if (!password.value) {
        mostrarError(password, 'La contraseña es requerida');
        esValido = false;
    }

    //Validamos que la contraseña tenga al menos 6 caracteres
    else if (password.value.length < 6) {
        mostrarError(password, 'La contraseña debe tener al menos 6 caracteres');
        esValido = false;
    }

    // Validar confirmación de contraseña
    if (!confirmPassword.value) {
        mostrarError(confirmPassword, 'Debes confirmar la contraseña');
        esValido = false;
    } else if (!validarCoincidencia(password.value, confirmPassword.value)) {
        mostrarError(confirmPassword, 'Las contraseñas no coinciden');
        esValido = false;
    }

    // Validar fecha de nacimiento no es futura y no está vacía
    if (!fecha.value) {
        mostrarError(fecha, 'La fecha de nacimiento es requerida');
        esValido = false;
    }
    if (fecha.value) {
        const fechaSeleccionada = new Date(fecha.value);
        const fechaActual = new Date();
        if (fechaSeleccionada > fechaActual) {
            mostrarError(fecha, 'La fecha de nacimiento no puede ser futura');
            esValido = false;
        }   
    }

    return esValido;
}

/**
 * Valida un formulario de crear producto
 * @param {HTMLElement} formulario - Elemento form
 * @returns {boolean} true si es válido
 */
function validarFormularioCrearProducto(formulario) {
    limpiarErrorsFormulario(formulario);
    let esValido = true;

    const titulo = formulario.querySelector('input[name="titulo"]');
    const precio = formulario.querySelector('input[name="precio"]');
    const categoria = formulario.querySelector('select[name="categoria"]');
    const descripcion = formulario.querySelector('textarea[name="descripcion"]');
    const lugarRecogida = formulario.querySelector('input[name="lugarRecogida"]');

    // Validar título
    if (!validarNoVacio(titulo.value)) {
        mostrarError(titulo, 'El título del producto es requerido');
        esValido = false;
    } else if (titulo.value.length < 3) {
        mostrarError(titulo, 'El título debe tener al menos 3 caracteres');
        esValido = false;
    }

    // Validar precio
    if (!precio.value) {
        mostrarError(precio, 'El precio es requerido');
        esValido = false;
    } else if (!validarNumeroPositivo(precio.value)) {
        mostrarError(precio, 'El precio debe ser mayor a 0€');
        esValido = false;
    }

    // Validar categoría
    if (!categoria.value) {
        mostrarError(categoria, 'Debes seleccionar una categoría');
        esValido = false;
    }

    // Validar descripción
    if (!validarNoVacio(descripcion.value)) {
        mostrarError(descripcion, 'La descripción es requerida');
        esValido = false;
    } else if (descripcion.value.length < 10) {
        mostrarError(descripcion, 'La descripción debe tener al menos 10 caracteres');
        esValido = false;
    }

    // Validar lugar de recogida
    if (!validarNoVacio(lugarRecogida.value)) {
        mostrarError(lugarRecogida, 'El lugar de recogida es requerido');
        esValido = false;
    }

    return esValido;
}

/**
 * Valida un formulario de editar producto
 * @param {HTMLElement} formulario - Elemento form
 * @returns {boolean} true si es válido
 */
function validarFormularioEditarProducto(formulario) {
    limpiarErrorsFormulario(formulario);
    let esValido = true;

    const precio = formulario.querySelector('input[name="precio"]');
    const descripcion = formulario.querySelector('textarea[name="descripcion"]');

    // Validar precio
    if (!precio.value) {
        mostrarError(precio, 'El precio es requerido');
        esValido = false;
    } else if (!validarNumeroPositivo(precio.value)) {
        mostrarError(precio, 'El precio debe ser mayor a 0€');
        esValido = false;
    }

    // Validar descripción
    if (!validarNoVacio(descripcion.value)) {
        mostrarError(descripcion, 'La descripción es requerida');
        esValido = false;
    } else if (descripcion.value.length < 10) {
        mostrarError(descripcion, 'La descripción debe tener al menos 10 caracteres');
        esValido = false;
    }

    return esValido;
}

/**
 * Valida un formulario de editar perfil
 * @param {HTMLElement} formulario - Elemento form
 * @returns {boolean} true si es válido
 */
function validarFormularioEditarPerfil(formulario) {
    limpiarErrorsFormulario(formulario);
    let esValido = true;

    const nombre = formulario.querySelector('input[name="nombre"]');
    const apellidos = formulario.querySelector('input[name="apellidos"]');
    const email = formulario.querySelector('input[name="email"]');
    const clave = formulario.querySelector('input[name="claveActual"]');
    const fecha = formulario.querySelector('input[name="fechaNacimiento"]');

    // Validar nombre
    if (!validarNoVacio(nombre.value)) {
        mostrarError(nombre, 'El nombre es requerido');
        esValido = false;
    }

    // Validar apellidos
    if (!validarNoVacio(apellidos.value)) {
        mostrarError(apellidos, 'Los apellidos son requeridos');
        esValido = false;
    }

    // Validar email
    if (!email.value.trim()) {
        mostrarError(email, 'El email es requerido');
        esValido = false;
    } else if (!validarEmail(email.value)) {
        mostrarError(email, 'Ingresa un email válido (ej: usuario@ejemplo.com)');
        esValido = false;
    }

    //Validamos que la fecha de nacimiento no sea futura y no este vacía
    if (!fecha.value) {
        mostrarError(fecha, 'La fecha de nacimiento es requerida');
        esValido = false;  
    }
    if (fecha.value) {
        const fechaSeleccionada = new Date(fecha.value);
        const fechaActual = new Date();
        if (fechaSeleccionada > fechaActual) {
            mostrarError(fecha, 'La fecha de nacimiento no puede ser futura');
            esValido = false;
        }
    }

    // Validar contraseña (requerida para confirmar cambios)
    if (!clave || !clave.value) {
        if (clave) mostrarError(clave, 'Debes ingresar tu contraseña para confirmar');
        esValido = false;
    }

    return esValido;
}

/**
 * Inicializa event listeners para limpiar errores cuando el usuario escriba
 * @param {HTMLElement} formulario - Elemento form
 */
function inicializarLimpiezaErrores(formulario) {
    const campos = formulario.querySelectorAll('input, textarea, select');
    campos.forEach(campo => {
        campo.addEventListener('input', function() {
            if (this.classList.contains('is-invalid')) {
                limpiarError(this);
            }
        });
        campo.addEventListener('change', function() {
            if (this.classList.contains('is-invalid')) {
                limpiarError(this);
            }
        });
    });
}

// Hacemos las funciones disponibles globalmente
window.validaciones = {
    validarEmail,
    validarNumeroPositivo,
    validarCoincidencia,
    validarNoVacio,
    mostrarError,
    limpiarError,
    limpiarErrorsFormulario,
    validarFormularioLogin,
    validarFormularioRegistro,
    validarFormularioCrearProducto,
    validarFormularioEditarProducto,
    validarFormularioEditarPerfil,
    inicializarLimpiezaErrores
};
