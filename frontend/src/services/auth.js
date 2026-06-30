export function saveLogin(token, email) {

    localStorage.setItem("token", token);
    localStorage.setItem("email", email);

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

}

export function isLoggedIn() {

    return getToken() !== null;

}