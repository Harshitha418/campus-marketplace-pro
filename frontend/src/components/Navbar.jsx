import { Link, useNavigate } from "react-router-dom";
import { isLoggedIn, logout } from "../services/auth";
import { useCart } from "../context/CartContext";

import {
    FaShoppingCart,
    FaHeart,
    FaClipboardList,
    FaSignOutAlt,
    FaUserCircle,
    FaBars
} from "react-icons/fa";

function Navbar() {

    const navigate = useNavigate();
    const { cartCount } = useCart();
    const handleLogout = () => {

        logout();
        navigate("/login");

    };

    return (

        <nav className="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm fixed-top">

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

                        <>

                        {/* Desktop view — unchanged, hidden on small screens */}
                        <div className="d-none d-md-flex align-items-center">

                            <Link
                                className="btn btn-outline-light me-3"
                                to="/account"
                            >
                                <FaUserCircle className="me-2" />
                                Account
                            </Link>

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
                                {cartCount > 0 && (
                                    <span className="badge bg-danger rounded-pill ms-2">
                                        {cartCount}
                                    </span>
                                )}
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

                        {/* Mobile view — hamburger dropdown, hidden on medium+ screens */}
                        <div className="d-md-none dropdown">

                            <button
                                className="btn btn-outline-light"
                                type="button"
                                data-bs-toggle="dropdown"
                            >
                                <FaBars />
                            </button>

                            <ul className="dropdown-menu dropdown-menu-end">

                                <li>
                                    <Link className="dropdown-item" to="/wishlist">
                                        <FaHeart className="me-2" /> Wishlist
                                    </Link>
                                </li>

                                <li>
                                    <Link className="dropdown-item" to="/cart">
                                        <FaShoppingCart className="me-2" /> Cart
                                    </Link>
                                </li>

                                <li>
                                    <Link className="dropdown-item" to="/orders">
                                        <FaClipboardList className="me-2" /> Orders
                                    </Link>
                                </li>

                                <li><hr className="dropdown-divider" /></li>

                                <li>
                                    <button className="dropdown-item text-danger" onClick={handleLogout}>
                                        <FaSignOutAlt className="me-2" /> Logout
                                    </button>
                                </li>

                            </ul>

                        </div>

                        </>

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