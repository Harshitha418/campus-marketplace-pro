import { useEffect, useState } from "react";
import api from "../services/api";
import { getEmail } from "../services/auth";

function Cart() {

    const [cartItems, setCartItems] = useState([]);

    useEffect(() => {

        loadCart();

    }, []);

    const loadCart = async () => {

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
            alert("Failed to load cart");

        }

    };

    const removeItem = async (id) => {

        try {

            await api.delete(`/cart/${id}`);

            loadCart();

        } catch (error) {

            console.log(error);
            alert("Failed to remove item");

        }

    };

    const total = cartItems.reduce(
        (sum, item) => sum + item.price * item.quantity,
        0
    );

    return (

        <div className="container mt-5">

            <h2 className="mb-4">

                🛒 My Cart

            </h2>

            {

                cartItems.length === 0 ?

                    <div className="alert alert-info">

                        Cart is empty.

                    </div>

                    :

                    <>

                        {

                            cartItems.map((item) => (

                                <div
                                    key={item.id}
                                    className="card shadow mb-3"
                                >

                                    <div className="card-body">

                                        <h4>

                                            📦 {item.title}

                                        </h4>

                                        <p>

                                            {item.description}

                                        </p>

                                        <span className="badge bg-primary">

                                            {item.category}

                                        </span>

                                        <h5 className="mt-3 text-success">

                                            ₹ {item.price}

                                        </h5>

                                        <h6>

                                            Quantity :

                                            {" "}

                                            {item.quantity}

                                        </h6>

                                        <h6>

                                            Subtotal :

                                            {" "}

                                            ₹ {item.price * item.quantity}

                                        </h6>

                                        <button
                                            className="btn btn-danger mt-3"
                                            onClick={() =>
                                                removeItem(item.id)
                                            }
                                        >

                                            Remove

                                        </button>

                                    </div>

                                </div>

                            ))

                        }

                        <div className="card shadow p-4 mt-4">

                            <h3>

                                Total : ₹ {total}

                            </h3>

                            <button
                                className="btn btn-success btn-lg mt-3"
                            >

                                Proceed to Checkout

                            </button>

                        </div>

                    </>

            }

        </div>

    );

}

export default Cart;