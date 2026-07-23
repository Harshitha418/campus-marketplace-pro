import { Link, useNavigate } from "react-router-dom";
import { isLoggedIn, logout, getRole } from "../services/auth";
import { useCart } from "../context/CartContext";

import {
    FaShoppingCart,
    FaHeart,
    FaClipboardList,
    FaSignOutAlt,
    FaUserCircle,
    FaBars,
    FaStore,
    FaTachometerAlt
} from "react-icons/fa";

function Navbar() {

    const navigate = useNavigate();
    const { cartCount } = useCart();
    const role = getRole();

    const handleLogout = () => {
        logout();
        navigate("/login");
    };

    return (

        <nav className="navbar navbar-dark bg-dark shadow-sm fixed-top">

            <div className="container">

                <Link className="navbar-brand fw-bold fs-4" to="/">
                    Campus Marketplace
                </Link>

                {isLoggedIn() ? (

                    <div className="d-flex align-items-center gap-2">

                        {/* Cart with live badge — always visible */}
                        <Link
                            className="btn btn-outline-light position-relative"
                            to="/cart"
                            title="Cart"
                        >
                            <FaShoppingCart />
                            {cartCount > 0 && (
                                <span className="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                                    {cartCount}
                                </span>
                            )}
                        </Link>

                        {/* Account icon -> account page */}
                        <Link
                            className="btn btn-outline-light"
                            to="/account"
                            title="My Account"
                        >
                            <FaUserCircle />
                        </Link>

                        {/* Hamburger menu with everything else */}
                        <div className="dropdown">

                            <button
                                className="btn btn-outline-light"
                                type="button"
                                data-bs-toggle="dropdown"
                                aria-expanded="false"
                            >
                                <FaBars />
                            </button>

                            <ul className="dropdown-menu dropdown-menu-end shadow">

                                {/* Seller-only entry point back to their dashboard */}
                                {role === "SELLER" && (
                                    <>
                                        <li>
                                            <Link className="dropdown-item fw-semibold" to="/seller">
                                                <FaStore className="me-2" /> My Shop
                                            </Link>
                                        </li>
                                        <li><hr className="dropdown-divider" /></li>
                                    </>
                                )}

                                {/* Admin-only entry point */}
                                {role === "ADMIN" && (
                                    <>
                                        <li>
                                            <Link className="dropdown-item fw-semibold" to="/admin">
                                                <FaTachometerAlt className="me-2" /> Admin Dashboard
                                            </Link>
                                        </li>
                                        <li><hr className="dropdown-divider" /></li>
                                    </>
                                )}

                                <li>
                                    <Link className="dropdown-item" to="/">
                                        <FaStore className="me-2" /> Browse Products
                                    </Link>
                                </li>

                                <li>
                                    <Link className="dropdown-item" to="/wishlist">
                                        <FaHeart className="me-2" /> Wishlist
                                    </Link>
                                </li>

                                <li>
                                    <Link className="dropdown-item" to="/orders">
                                        <FaClipboardList className="me-2" /> My Orders
                                    </Link>
                                </li>

                                <li><hr className="dropdown-divider" /></li>

                                <li>
                                    <button
                                        className="dropdown-item text-danger"
                                        onClick={handleLogout}
                                    >
                                        <FaSignOutAlt className="me-2" /> Logout
                                    </button>
                                </li>

                            </ul>

                        </div>

                    </div>

                ) : (

                    <div>
                        <Link className="btn btn-outline-light me-2" to="/login">
                            Login
                        </Link>
                        <Link className="btn btn-warning" to="/register">
                            Register
                        </Link>
                    </div>

                )}

            </div>

        </nav>

    );

}

export default Navbar;