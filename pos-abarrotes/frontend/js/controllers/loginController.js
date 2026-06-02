import { login } from '../services/authApi.js';
const form = document.getElementById('loginForm');
const msg = document.getElementById('msg');
form.addEventListener('submit', async (e) => {
    e.preventDefault(); msg.innerHTML = '';
    try {
        const usuario = await login(form.username.value.trim(), form.password.value);
        localStorage.setItem('usuario', JSON.stringify(usuario));
        window.location.href = 'menu.html';
    } catch (err) { msg.innerHTML = `<div class="alert alert-danger">${err.message}</div>`; }
});
