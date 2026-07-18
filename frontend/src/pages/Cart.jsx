import { useEffect, useState } from "react";
import api from "../services/api";
import { FaTrash, FaTag } from "react-icons/fa";
import { toast } from "react-toastify";
import LoadingSpinner from "../components/LoadingSpinner";
import EmptyState from "../components/EmptyState";
import ProductImage from "../components/ProductImage";
import { useCart } from "../context/CartContext";
import { useNavigate } from "react-router-dom";

function Cart() {

    const [cartItems, setCartItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const { refreshCart } = useCart();
    const navigate = useNavigate();
    const [checkingOut, setCheckingOut] = useState(false);

    useEffect(() => {
        loadCart();
    }, []);

    const loadCart = async () => {

        setLoading(true);

        try {

            const response = await api.get("/cart");

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

    const handleCheckout = async () => {

        setCheckingOut(true);

        try {

            // Step 1: ask our backend to create a Razorpay order for the cart total.
            const orderRes = await api.post("/payment/create-order", null, {
                params: { amount: total }
            });

            const { orderId, amount, currency, keyId } = orderRes.data;

            // Step 2: configure and open the Razorpay checkout popup.
            const options = {
                key: keyId,
                amount: amount,
                currency: currency,
                order_id: orderId,
                name: "Campus Marketplace",
                description: "Order Payment",

                // Step 3 -> 4: this runs after the user pays successfully.
                handler: async function (response) {

                    try {

                        // Send the payment proof back for signature verification.
                        const verifyRes = await api.post("/payment/verify", {
                            razorpayOrderId: response.razorpay_order_id,
                            razorpayPaymentId: response.razorpay_payment_id,
                            razorpaySignature: response.razorpay_signature
                        });

                        toast.success(verifyRes.data);

                        refreshCart();

                        navigate("/orders");

                    } catch (err) {
                        toast.error("Payment verification failed");
                    }

                },

                theme: { color: "#198754" }
            };

            const razorpay = new window.Razorpay(options);

            // If the user closes the popup without paying.
            razorpay.on("payment.failed", function () {
                toast.error("Payment failed. Please try again.");
            });

            razorpay.open();

        } catch (error) {

            toast.error(
                error.response?.data?.message || "Could not start payment"
            );

        } finally {

            setCheckingOut(false);

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
                                    onClick={handleCheckout}
                                    disabled={checkingOut}
                                >

                                    {checkingOut ? "Placing Order..." : "Proceed to Checkout"}

                                </button>

                            </div>

                        </div>

                    </>

            }

        </div>

    );

}

export default Cart;