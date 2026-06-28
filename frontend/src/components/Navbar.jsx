import { Link } from "react-router-dom";

function Navbar() {
    return (
        <nav className="navbar navbar-dark bg-dark px-4">
            <Link className="navbar-brand" to="/">
                Campus Marketplace
            </Link>

            <div>
                <Link className="btn btn-outline-light me-2" to="/login">
                    Login
                </Link>

                <Link className="btn btn-warning" to="/register">
                    Register
                </Link>
            </div>
        </nav>
    );
}

export default Navbar;