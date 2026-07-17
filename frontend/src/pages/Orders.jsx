import { useEffect, useState } from "react";
import api from "../services/api";
import { FaTag } from "react-icons/fa";
import { toast } from "react-toastify";

import LoadingSpinner from "../components/LoadingSpinner";
import EmptyState from "../components/EmptyState";
import ProductImage from "../components/ProductImage";

function Orders() {

    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {

        loadOrders();

    }, []);

    const loadOrders = async () => {

        setLoading(true);

        try {

            const response = await api.get(
                "/orders"
            );

            setOrders(response.data);

        } catch (error) {

            console.log(error);

            toast.error("Failed to load orders");

        } finally {

            setLoading(false);

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

    if (loading) {

        return (

            <LoadingSpinner
                message="Loading your orders..."
            />

        );

    }

    return (

        <div className="container py-5">

            <h2 className="fw-bold mb-4">

                📦 My Orders

            </h2>

            {

                orders.length === 0 ?

                    <EmptyState

                        icon="📦"

                        title="No Orders Yet"

                        message="Your purchased products will appear here."

                        buttonText="Continue Shopping"

                        buttonLink="/"

                    />

                    :

                    orders.map((order) => (

                        <div
                            key={order.id}
                            className="card border-0 shadow rounded-4 mb-4"
                        >

                            <div className="card-body">

                                <div className="row align-items-center">

                                    <div className="col-md-2 text-center">

                                        <ProductImage
                                            title={order.title}
                                            imageUrl={order.imageUrl}
                                        />

                                    </div>

                                    <div className="col-md-7">

                                        <h4 className="fw-bold">

                                            {order.title}

                                        </h4>

                                        <p className="text-muted">

                                            {order.description}

                                        </p>

                                        <span className="badge bg-primary rounded-pill">

                                            <FaTag className="me-2" />

                                            {order.category}

                                        </span>

                                        <h4 className="text-success mt-3 fw-bold">

                                            ₹ {order.price}

                                        </h4>

                                    </div>

                                    <div className="col-md-3 text-end">

                                        <p>

                                            Quantity

                                        </p>

                                        <span className="badge bg-dark fs-6">

                                            {order.quantity}

                                        </span>

                                        <div className="mt-3">

                                            <span
                                                className={`badge ${getStatusBadge(order.status)} fs-6 px-3 py-2`}
                                            >

                                                {order.status}

                                            </span>

                                        </div>

                                    </div>

                                </div>

                            </div>

                        </div>

                    ))

            }

        </div>

    );

}

export default Orders;