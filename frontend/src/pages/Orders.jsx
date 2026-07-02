import { useEffect, useState } from "react";
import api from "../services/api";
import { getEmail } from "../services/auth";

function Orders() {

    const [orders, setOrders] = useState([]);

    useEffect(() => {

        loadOrders();

    }, []);

    const loadOrders = async () => {

        try {

            const response = await api.get(
                "/orders",
                {
                    params: {
                        email: getEmail()
                    }
                }
            );

            setOrders(response.data);

        } catch (error) {

            console.log(error);

            alert("Failed to load orders");

        }

    };

    return (

        <div className="container mt-5">

            <h2 className="mb-4">

                📦 My Orders

            </h2>

            {

                orders.length === 0 ?

                    <div className="alert alert-info">

                        No orders yet.

                    </div>

                    :

                    orders.map((order) => (

                        <div
                            key={order.id}
                            className="card shadow mb-3"
                        >

                            <div className="card-body">

                                <h4>

                                    📦 {order.title}

                                </h4>

                                <p>

                                    {order.description}

                                </p>

                                <span className="badge bg-primary">

                                    {order.category}

                                </span>

                                <h5 className="text-success mt-3">

                                    ₹ {order.price}

                                </h5>

                                <h6>

                                    Quantity : {order.quantity}

                                </h6>

                                <h6>

                                    Status :
                                    {" "}
                                    <span className="text-success">

                                        {order.status}

                                    </span>

                                </h6>

                            </div>

                        </div>

                    ))

            }

        </div>

    );

}

export default Orders;