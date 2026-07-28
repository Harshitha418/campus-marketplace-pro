import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../services/api";
import { toast } from "react-toastify";
import LoadingSpinner from "../components/LoadingSpinner";
import ProductImage from "../components/ProductImage";
import { FaArrowLeft } from "react-icons/fa";

function OrderDetails() {

    const { orderId } = useParams();
    const navigate = useNavigate();

    const [order, setOrder] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        loadOrder();
    }, []);

    const loadOrder = async () => {

        setLoading(true);

        try {
            const response = await api.get(`/orders/${orderId}`);
            setOrder(response.data);
        } catch (error) {
            console.log(error);
            toast.error("Could not load order");
        } finally {
            setLoading(false);
        }

    };

    const statusBadge = (status) => {
        switch (status) {
            case "PLACED": return "bg-warning text-dark";
            case "SHIPPED": return "bg-primary";
            case "DELIVERED": return "bg-success";
            case "CANCELLED": return "bg-danger";
            default: return "bg-secondary";
        }
    };

    const formatDate = (dateStr) =>
        new Date(dateStr).toLocaleString("en-IN", {
            day: "numeric", month: "short", year: "numeric",
            hour: "2-digit", minute: "2-digit"
        });

    if (loading) return <LoadingSpinner message="Loading order..." />;
    if (!order) return null;

    return (

        <div className="container py-5">

            <button
                className="btn btn-outline-dark btn-sm mb-3"
                onClick={() => navigate("/orders")}
            >
                <FaArrowLeft className="me-2" /> Back to orders
            </button>

            {/* Order header / receipt summary */}
            <div className="card border-0 shadow-sm rounded-4 mb-4">
                <div className="card-body p-4">

                    <div className="d-flex flex-wrap justify-content-between">

                        <div>
                            <h4 className="fw-bold mb-1">Order #{order.orderId}</h4>
                            <p className="text-muted mb-0">{formatDate(order.createdAt)}</p>
                        </div>

                        <div className="text-end">
                            <h4 className="fw-bold text-success mb-1">
                                ₹ {order.totalAmount?.toFixed(2)}
                            </h4>
                            <p className="text-muted small mb-0">
                                Txn: {order.transactionId || "—"}
                            </p>
                        </div>

                    </div>

                </div>
            </div>

            {/* Line items */}
            <h5 className="fw-bold mb-3">Items ({order.items.length})</h5>

            {order.items.map((item) => (

                <div key={item.itemId} className="card border-0 shadow-sm rounded-4 mb-3">
                    <div className="card-body">

                        <div className="row align-items-center">

                            <div className="col-md-2 text-center">
                                <ProductImage title={item.title} imageUrl={item.imageUrl} />
                            </div>

                            <div className="col-md-6">
                                <h6 className="fw-bold mb-1">{item.title}</h6>
                                <p className="text-muted small mb-0">
                                    Qty: {item.quantity} &times; ₹ {item.priceAtPurchase?.toFixed(2)}
                                </p>
                            </div>

                            <div className="col-md-2 fw-bold text-success">
                                ₹ {(item.quantity * item.priceAtPurchase).toFixed(2)}
                            </div>

                            <div className="col-md-2 text-end">
                                <span className={`badge ${statusBadge(item.status)} px-3 py-2`}>
                                    {item.status}
                                </span>
                            </div>

                        </div>

                    </div>
                </div>

            ))}

        </div>

    );

}

export default OrderDetails;