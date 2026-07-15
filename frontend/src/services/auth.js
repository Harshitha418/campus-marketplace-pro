export function saveLogin(token, email, role) {

    localStorage.setItem("token", token);
    localStorage.setItem("email", email);
    localStorage.setItem("role", role);

}

export function getRole() {

    return localStorage.getItem("role");

}

export function getToken() {

    return localStorage.getItem("token");

}

export function getEmail() {

    return localStorage.getItem("email");

}

export function logout() {

    localStorage.removeItem("token");
    localStorage.removeItem("email");
    localStorage.removeItem("role");
}

export function isLoggedIn() {

    return getToken() !== null;

}