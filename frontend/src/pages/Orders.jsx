import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";
import { toast } from "react-toastify";
import LoadingSpinner from "../components/LoadingSpinner";
import EmptyState from "../components/EmptyState";
import { FaChevronRight } from "react-icons/fa";

function Orders() {

    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        loadOrders();
    }, []);

    const loadOrders = async () => {

        setLoading(true);

        try {
            const response = await api.get("/orders");
            setOrders(response.data);
        } catch (error) {
            console.log(error);
            toast.error("Failed to load orders");
        } finally {
            setLoading(false);
        }

    };

    // "3 days ago", "2 hours ago", etc.
    const timeAgo = (dateStr) => {
        const then = new Date(dateStr);
        const secs = Math.floor((Date.now() - then) / 1000);
        const mins = Math.floor(secs / 60);
        const hours = Math.floor(mins / 60);
        const days = Math.floor(hours / 24);
        const months = Math.floor(days / 30);
        if (months > 0) return `${months} month${months > 1 ? "s" : ""} ago`;
        if (days > 0) return `${days} day${days > 1 ? "s" : ""} ago`;
        if (hours > 0) return `${hours} hour${hours > 1 ? "s" : ""} ago`;
        if (mins > 0) return `${mins} minute${mins > 1 ? "s" : ""} ago`;
        return "just now";
    };

    const formatDate = (dateStr) => {
        return new Date(dateStr).toLocaleDateString("en-IN", {
            day: "numeric", month: "short", year: "numeric"
        });
    };

    if (loading) {
        return <LoadingSpinner message="Loading your orders..." />;
    }

    if (orders.length === 0) {
        return (
            <div className="container py-5">
                <EmptyState
                    icon="📦"
                    title="No orders yet"
                    message="Your placed orders will appear here."
                    buttonText="Start Shopping"
                    buttonLink="/"
                />
            </div>
        );
    }

    return (

        <div className="container py-5">

            <h2 className="fw-bold mb-4">📦 My Orders</h2>

            <div className="card border-0 shadow-sm rounded-4">

                <div className="table-responsive">

                    <table className="table table-hover align-middle mb-0">

                        <thead>
                            <tr>
                                <th className="px-4 py-3">Order ID</th>
                                <th className="py-3">Date</th>
                                <th className="py-3">Items</th>
                                <th className="py-3">Total</th>
                                <th className="py-3"></th>
                            </tr>
                        </thead>

                        <tbody>

                            {orders.map((order) => (

                                <tr
                                    key={order.orderId}
                                    style={{ cursor: "pointer" }}
                                    onClick={() => navigate(`/orders/${order.orderId}`)}
                                >

                                    <td className="px-4 py-3 fw-semibold">
                                        #{order.orderId}
                                    </td>

                                    <td className="py-3">
                                        {formatDate(order.createdAt)}
                                        <div className="text-muted small">
                                            {timeAgo(order.createdAt)}
                                        </div>
                                    </td>

                                    <td className="py-3">
                                        {order.itemCount} item{order.itemCount > 1 ? "s" : ""}
                                    </td>

                                    <td className="py-3 fw-bold text-success">
                                        ₹ {order.totalAmount?.toFixed(2)}
                                    </td>

                                    <td className="py-3 text-end px-4">
                                        <FaChevronRight className="text-muted" />
                                    </td>

                                </tr>

                            ))}

                        </tbody>

                    </table>

                </div>

            </div>

        </div>

    );

}

export default Orders;