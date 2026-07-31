import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";
import { toast } from "react-toastify";
import LoadingSpinner from "../components/LoadingSpinner";
import { FaChevronRight } from "react-icons/fa";

function AdminDashboard() {

    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        loadOrders();
    }, []);

    const loadOrders = async () => {
        setLoading(true);
        try {
            const response = await api.get("/orders/all");
            setOrders(response.data);
        } catch (error) {
            toast.error("Failed to load orders");
        } finally {
            setLoading(false);
        }
    };

    const formatDate = (dateStr) =>
        new Date(dateStr).toLocaleDateString("en-IN", {
            day: "numeric", month: "short", year: "numeric"
        });

    if (loading) return <LoadingSpinner message="Loading all orders..." />;

    return (

        <div className="container py-5">

            <h2 className="fw-bold mb-1">Admin Dashboard</h2>
            <p className="text-muted mb-4">{orders.length} total order(s)</p>

            {orders.length === 0 ? (

                <div className="card border-0 shadow-sm rounded-4">
                    <div className="card-body text-center py-5">
                        <h5 className="text-muted">No orders yet</h5>
                    </div>
                </div>

            ) : (

                <div className="card border-0 shadow-sm rounded-4">
                    <div className="table-responsive">
                        <table className="table table-hover align-middle mb-0">

                            <thead>
                                <tr>
                                    <th className="px-4 py-3">Order ID</th>
                                    <th className="py-3">Buyer</th>
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
                                        onClick={() => navigate(`/admin/order/${order.orderId}`)}
                                    >
                                        <td className="px-4 py-3 fw-semibold">#{order.orderId}</td>
                                        <td className="py-3">{order.userEmail}</td>
                                        <td className="py-3">{formatDate(order.createdAt)}</td>
                                        <td className="py-3">{order.itemCount}</td>
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

            )}

        </div>

    );

}

export default AdminDashboard;