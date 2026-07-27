import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";
import { toast } from "react-toastify";
import LoadingSpinner from "../components/LoadingSpinner";
import { FaUser, FaBoxOpen, FaChevronRight } from "react-icons/fa";

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

    // Build one summary row per buyer: email, active count, total count.
    const summary = Object.values(
        orders.reduce((acc, o) => {
            const email = o.userEmail || "Unknown";
            if (!acc[email]) {
                acc[email] = { email, total: 0, active: 0 };
            }
            acc[email].total += 1;
            if (o.status !== "DELIVERED" && o.status !== "CANCELLED") {
                acc[email].active += 1;
            }
            return acc;
        }, {})
    ).sort((a, b) => b.active - a.active);

    if (loading) {
        return <LoadingSpinner message="Loading buyers..." />;
    }

    return (

        <div className="container py-5">

            <h2 className="fw-bold mb-1">Admin Dashboard</h2>
            <p className="text-muted mb-4">
                {summary.length} buyer(s) &middot; {orders.length} total order(s)
            </p>

            {summary.length === 0 ? (

                <div className="card border-0 shadow-sm rounded-4">
                    <div className="card-body text-center py-5">
                        <h5 className="text-muted">No orders yet</h5>
                    </div>
                </div>

            ) : (

                <div className="row">

                    {summary.map((buyer) => (

                        <div key={buyer.email} className="col-lg-4 col-md-6 mb-4">

                            <div
                                className="card border-0 shadow-sm rounded-4 h-100"
                                style={{ cursor: "pointer", transition: "transform .15s, box-shadow .15s" }}
                                onClick={() => navigate(`/admin/user/${encodeURIComponent(buyer.email)}`)}
                                onMouseEnter={(e) => {
                                    e.currentTarget.style.transform = "translateY(-4px)";
                                    e.currentTarget.style.boxShadow = "0 .5rem 1.5rem rgba(0,0,0,.12)";
                                }}
                                onMouseLeave={(e) => {
                                    e.currentTarget.style.transform = "";
                                    e.currentTarget.style.boxShadow = "";
                                }}
                            >

                                <div className="card-body d-flex align-items-center">

                                    <div
                                        className="rounded-circle bg-dark text-white d-flex align-items-center justify-content-center me-3"
                                        style={{ width: "48px", height: "48px", flexShrink: 0 }}
                                    >
                                        <FaUser />
                                    </div>

                                    <div className="flex-grow-1 text-truncate">
                                        <h6 className="fw-bold mb-1 text-truncate">{buyer.email}</h6>
                                        <span className="text-muted small">
                                            <FaBoxOpen className="me-1" />
                                            {buyer.active} active &middot; {buyer.total} total
                                        </span>
                                    </div>

                                    <FaChevronRight className="text-muted ms-2" />

                                </div>

                            </div>

                        </div>

                    ))}

                </div>

            )}

        </div>

    );

}

export default AdminDashboard;