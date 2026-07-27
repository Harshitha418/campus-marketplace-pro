import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../services/api";
import { toast } from "react-toastify";
import LoadingSpinner from "../components/LoadingSpinner";
import ProductImage from "../components/ProductImage";
import { FaArrowLeft } from "react-icons/fa";

const STATUSES = ["PLACED", "SHIPPED", "DELIVERED", "CANCELLED"];

function AdminUserOrders() {

    const { email } = useParams();
    const navigate = useNavigate();
    const decodedEmail = decodeURIComponent(email);

    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        loadOrders();
    }, []);

    const loadOrders = async () => {

        setLoading(true);

        try {
            const response = await api.get("/orders/all");
            // Only this buyer's orders.
            setOrders(response.data.filter((o) => o.userEmail === decodedEmail));
        } catch (error) {
            toast.error("Failed to load orders");
        } finally {
            setLoading(false);
        }

    };

    const updateStatus = async (id, status) => {

        try {
            await api.put(`/orders/${id}`, null, { params: { status } });
            toast.success(`Order marked ${status}`);
            loadOrders();
        } catch (error) {
            toast.error(error.response?.data?.message || "Failed to update status");
        }

    };

    const getStatusBadge = (status) => {
        switch (status) {
            case "PLACED": return "bg-warning text-dark";
            case "SHIPPED": return "bg-primary";
            case "DELIVERED": return "bg-success";
            case "CANCELLED": return "bg-danger";
            default: return "bg-secondary";
        }
    };

    if (loading) {
        return <LoadingSpinner message="Loading orders..." />;
    }

    return (

        <div className="container py-5">

            <button
                className="btn btn-outline-dark btn-sm mb-3"
                onClick={() => navigate("/admin")}
            >
                <FaArrowLeft className="me-2" /> Back to buyers
            </button>

            <h2 className="fw-bold mb-1">{decodedEmail}</h2>
            <p className="text-muted mb-4">{orders.length} order(s)</p>

            {orders.map((order) => (

                <div key={order.id} className="card border-0 shadow-sm rounded-4 mb-3">

                    <div className="card-body">

                        <div className="row align-items-center">

                            <div className="col-md-2 text-center">
                                <ProductImage title={order.title} imageUrl={order.imageUrl} />
                            </div>

                            <div className="col-md-5">
                                <h6 className="fw-bold mb-1">{order.title}</h6>
                                <p className="text-muted small mb-2">
                                    Order #{order.id} &middot; Qty: {order.quantity}
                                </p>
                                <h6 className="text-success fw-bold mb-0">
                                    ₹ {(order.price * order.quantity).toFixed(2)}
                                </h6>
                            </div>

                            <div className="col-md-2 text-center">
                                <span className={`badge ${getStatusBadge(order.status)} px-3 py-2`}>
                                    {order.status}
                                </span>
                            </div>

                            <div className="col-md-3">
                                <select
                                    className="form-select form-select-sm"
                                    value={order.status}
                                    onChange={(e) => updateStatus(order.id, e.target.value)}
                                >
                                    {STATUSES.map((s) => (
                                        <option key={s} value={s}>{s}</option>
                                    ))}
                                </select>
                            </div>

                        </div>

                    </div>

                </div>

            ))}

        </div>

    );

}

export default AdminUserOrders;