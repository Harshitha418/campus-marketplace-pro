import { useEffect, useState } from "react";
import api from "../services/api";
import { getEmail } from "../services/auth";
import { FaTrash, FaTag } from "react-icons/fa";
import { Link } from "react-router-dom";
import { toast } from "react-toastify";
import LoadingSpinner from "../components/LoadingSpinner";
import EmptyState from "../components/EmptyState";
import ProductImage from "../components/ProductImage";
import { useCart } from "../context/CartContext";

function Cart() {

    const [cartItems, setCartItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const { refreshCart } = useCart();

    useEffect(() => {
        loadCart();
    }, []);

    const loadCart = async () => {

        setLoading(true);

        try {

            const response = await api.get(
                "/cart",
                {
                    params: {
                        email: getEmail()
                    }
                }
            );

            setCartItems(response.data);

        } catch (error) {

            console.log(error);
            toast.error("Failed to load cart");

        } finally {

            setLoading(false);

        }

    };

    const removeItem = async (id) => {

        try {

            await api.delete(`/cart/${id}`);

            toast.success("Item removed");

            loadCart();
            refreshCart();

        } catch (error) {

            console.log(error);
            toast.error("Failed to remove item");

        }

    };

    const total = cartItems.reduce(
        (sum, item) => sum + item.price * item.quantity,
        0
    );

    if (loading) {

    return (

        <LoadingSpinner
            message="Loading your cart..."
        />

    );

}

    return (

        <div className="container py-5">

            <h2 className="fw-bold mb-4">

                🛒 My Cart

            </h2>

            {

                cartItems.length === 0 ?

                    <EmptyState

    icon="🛒"

    title="Your cart is empty"

    message="Browse products and add something!"

    buttonText="Continue Shopping"

    buttonLink="/"

/>

                    :

                    <>

                        {

                            cartItems.map((item) => (

                                <div
                                    key={item.id}
                                    className="card shadow border-0 rounded-4 mb-4"
                                >

                                    <div className="card-body">

                                        <div className="row align-items-center">

                                            <div className="col-md-2 text-center">

                                                <ProductImage
                                                    title={item.title}
                                                    imageUrl={item.imageUrl}
                                                />

                                            </div>

                                            <div className="col-md-7">

                                                <h4 className="fw-bold">

                                                    {item.title}

                                                </h4>

                                                <p className="text-muted">

                                                    {item.description}

                                                </p>

                                                <span className="badge bg-primary rounded-pill">

                                                    <FaTag className="me-2" />

                                                    {item.category}

                                                </span>

                                            </div>

                                            <div className="col-md-3 text-end">

                                                <h4 className="text-success fw-bold">

                                                    ₹ {item.price}

                                                </h4>

                                                <p>

                                                    Quantity

                                                    <span className="badge bg-dark ms-2">

                                                        {item.quantity}

                                                    </span>

                                                </p>

                                                <h6 className="text-muted">

                                                    Subtotal

                                                </h6>

                                                <h5 className="fw-bold">

                                                    ₹ {(item.price * item.quantity).toFixed(2)}

                                                </h5>

                                                <button
                                                    className="btn btn-outline-danger rounded-pill mt-2"
                                                    onClick={() => removeItem(item.id)}
                                                >

                                                    <FaTrash className="me-2" />

                                                    Remove

                                                </button>

                                            </div>

                                        </div>

                                    </div>

                                </div>

                            ))

                        }

                        <div className="card border-0 shadow rounded-4">

                            <div className="card-body text-center p-4">

                                <h5 className="text-muted">

                                    Total Amount

                                </h5>

                                <h2 className="text-success fw-bold">

                                    ₹ {total.toFixed(2)}

                                </h2>

                                <button
                                    className="btn btn-success btn-lg rounded-pill px-5 mt-3"
                                >

                                    Proceed to Checkout

                                </button>

                            </div>

                        </div>

                    </>

            }

        </div>

    );

}

export default Cart;