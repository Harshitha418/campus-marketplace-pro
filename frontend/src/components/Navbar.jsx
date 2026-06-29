import { Link, useNavigate } from "react-router-dom";
import { isLoggedIn, logout } from "../services/auth";

function Navbar() {

    const navigate = useNavigate();

    const handleLogout = () => {

        logout();

        navigate("/login");

    };

    return (

        <nav className="navbar navbar-dark bg-dark px-4">

            <Link
                className="navbar-brand"
                to="/">

                Campus Marketplace

            </Link>

            {

                isLoggedIn()

                    ?

                    <div>

                        <Link
                            className="btn btn-outline-light me-2"
                            to="/wishlist">

                            Wishlist

                        </Link>

                        <Link
                            className="btn btn-outline-light me-2"
                            to="/cart">

                            Cart

                        </Link>

                        <Link
                            className="btn btn-outline-light me-2"
                            to="/orders">

                            Orders

                        </Link>

                        <button
                            className="btn btn-danger"
                            onClick={handleLogout}>

                            Logout

                        </button>

                    </div>

                    :

                    <div>

                        <Link
                            className="btn btn-outline-light me-2"
                            to="/login">

                            Login

                        </Link>

                        <Link
                            className="btn btn-warning"
                            to="/register">

                            Register

                        </Link>

                    </div>

            }

        </nav>

    );

}

export default Navbar;