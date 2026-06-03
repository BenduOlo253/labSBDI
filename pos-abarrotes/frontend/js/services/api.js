export const API_BASE_URL = "http://localhost:8080/api";
// Para probar desde celular en la misma red, cambia la URL anterior por la IP de tu equipo:
// export const API_BASE_URL = "http://192.168.0.8:8080/api";

export async function request(endpoint, options = {}) {
    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
        headers: {
            "Content-Type": "application/json",
            ...(options.headers || {})
        },
        ...options
    });

    if (!response.ok) {
        const error = await response.json().catch(() => null);
        throw new Error(error?.mensaje || error?.message || "Error en la solicitud");
    }

    if (response.status === 204) return null;
    return response.json();
}

export function requireSession() {
    const raw = localStorage.getItem("usuario");
    if (!raw) {
        window.location.href = "login.html";
        return null;
    }

    try {
        return JSON.parse(raw);
    } catch {
        localStorage.removeItem("usuario");
        window.location.href = "login.html";
        return null;
    }
}

export function logout() {
    localStorage.removeItem("usuario");
    localStorage.removeItem("turnoActivo");
    window.location.href = "login.html";
}
