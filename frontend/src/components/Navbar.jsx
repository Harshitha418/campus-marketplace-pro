import { Link, useNavigate } from "react-router-dom";
import { isLoggedIn, logout, getEmail } from "../services/auth";
import {
    FaShoppingCart,
    FaHeart,
    FaClipboardList,
    FaSignOutAlt,
    FaUserCircle
} from "react-icons/fa";

function Navbar() {

    const navigate = useNavigate();

    const handleLogout = () => {

        logout();
        navigate("/login");

    };

    return (

        <nav className="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm">

            <div className="container">

                <Link
                    className="navbar-brand fw-bold fs-3"
                    to="/"
                >
                    Campus Marketplace
                </Link>

                {

                    isLoggedIn()

                        ?

                        <div className="d-flex align-items-center">

                            <span
                                className="text-white me-4 d-none d-md-block"
                            >
                                <FaUserCircle className="me-2" />
                                {getEmail()}
                            </span>

                            <Link
                                className="btn btn-outline-light me-2"
                                to="/wishlist"
                            >
                                <FaHeart /> Wishlist
                            </Link>

                            <Link
                                className="btn btn-outline-light me-2"
                                to="/cart"
                            >
                                <FaShoppingCart /> Cart
                            </Link>

                            <Link
                                className="btn btn-outline-light me-2"
                                to="/orders"
                            >
                                <FaClipboardList /> Orders
                            </Link>

                            <button
                                className="btn btn-danger"
                                onClick={handleLogout}
                            >
                                <FaSignOutAlt /> Logout
                            </button>

                        </div>

                        :

                        <div>

                            <Link
                                className="btn btn-outline-light me-2"
                                to="/login"
                            >
                                Login
                            </Link>

                            <Link
                                className="btn btn-warning"
                                to="/register"
                            >
                                Register
                            </Link>

                        </div>

                }

            </div>

        </nav>

    );

}

export default Navbar;