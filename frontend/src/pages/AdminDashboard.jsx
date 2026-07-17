import { useEffect, useState } from "react";
import api from "../services/api";
import { toast } from "react-toastify";
import LoadingSpinner from "../components/LoadingSpinner";
import ProductImage from "../components/ProductImage";

const STATUSES = ["PLACED", "SHIPPED", "DELIVERED", "CANCELLED"];

function AdminDashboard() {

    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [filter, setFilter] = useState("ALL");

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

    const updateStatus = async (id, status) => {

        try {

            await api.put(`/orders/${id}/status`, null, {
                params: { status }
            });

            toast.success(`Order marked ${status}`);

            loadOrders();

        } catch (error) {

            toast.error(
                error.response?.data?.message || "Failed to update status"
            );

        }

    };

    const getStatusBadge = (status) => {

        switch (status) {
            case "PLACED":
                return "bg-warning text-dark";
            case "SHIPPED":
                return "bg-primary";
            case "DELIVERED":
                return "bg-success";
            case "CANCELLED":
                return "bg-danger";
            default:
                return "bg-secondary";
        }

    };

    const filteredOrders = filter === "ALL"
        ? orders
        : orders.filter((o) => o.status === filter);

    if (loading) {
        return <LoadingSpinner message="Loading all orders..." />;
    }

    return (

        <div className="container py-5">

            <h2 className="fw-bold mb-1">Admin Dashboard</h2>
            <p className="text-muted mb-4">
                Managing {orders.length} order(s) across all users
            </p>

            {/* Status filter */}
            <div className="d-flex flex-wrap gap-2 mb-4">

                <button
                    className={`btn btn-sm ${
                        filter === "ALL" ? "btn-dark" : "btn-outline-dark"
                    }`}
                    onClick={() => setFilter("ALL")}
                >
                    All ({orders.length})
                </button>

                {STATUSES.map((s) => (
                    <button
                        key={s}
                        className={`btn btn-sm ${
                            filter === s ? "btn-primary" : "btn-outline-primary"
                        }`}
                        onClick={() => setFilter(s)}
                    >
                        {s} ({orders.filter((o) => o.status === s).length})
                    </button>
                ))}

            </div>

            {filteredOrders.length === 0 ? (

                <div className="card border-0 shadow-sm rounded-4">
                    <div className="card-body text-center py-5">
                        <h5 className="text-muted">No orders in this category</h5>
                    </div>
                </div>

            ) : (

                filteredOrders.map((order) => (

                    <div key={order.id} className="card border-0 shadow rounded-4 mb-3">

                        <div className="card-body">

                            <div className="row align-items-center">

                                <div className="col-md-2 text-center">
                                    <ProductImage
                                        title={order.title}
                                        imageUrl={order.imageUrl}
                                    />
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
                                    <span
                                        className={`badge ${getStatusBadge(order.status)} px-3 py-2`}
                                    >
                                        {order.status}
                                    </span>
                                </div>

                                <div className="col-md-3">

                                    <select
                                        className="form-select form-select-sm"
                                        value={order.status}
                                        onChange={(e) =>
                                            updateStatus(order.id, e.target.value)
                                        }
                                    >
                                        {STATUSES.map((s) => (
                                            <option key={s} value={s}>
                                                {s}
                                            </option>
                                        ))}
                                    </select>

                                </div>

                            </div>

                        </div>

                    </div>

                ))

            )}

        </div>

    );

}

export default AdminDashboard;