import { useEffect, useState } from "react";
import api from "../services/api";
import { getEmail } from "../services/auth";

function Wishlist() {

    const [wishlist, setWishlist] = useState([]);

    useEffect(() => {
        loadWishlist();
    }, []);

    const loadWishlist = async () => {

        try {

            const response = await api.get(
                "/wishlist",
                {
                    params: {
                        email: getEmail()
                    }
                }
            );

            setWishlist(response.data);

        } catch (error) {

            console.log(error);
            alert("Failed to load wishlist");

        }

    };

    const removeItem = async (id) => {

        try {

            await api.delete(`/wishlist/${id}`);

            loadWishlist();

        } catch (error) {

            console.log(error);
            alert("Failed to remove item");

        }

    };

    const moveToCart = async (productId) => {

        try {

            await api.post(
                `/cart/${productId}`,
                null,
                {
                    params: {
                        email: getEmail()
                    }
                }
            );

            alert("🛒 Added to Cart!");

        } catch (error) {

            console.log(error);
            alert("Failed to add to cart");

        }

    };

    return (

        <div className="container mt-5">

            <h2 className="mb-4">
                ❤️ My Wishlist
            </h2>

            {
                wishlist.length === 0 ?

                    <div className="alert alert-info">

                        Wishlist is empty.

                    </div>

                    :

                    wishlist.map((item) => (

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

                                <button
                                    className="btn btn-success me-2 mt-3"
                                    onClick={() => moveToCart(item.productId)}
                                >
                                    🛒 Move to Cart
                                </button>

                                <button
                                    className="btn btn-danger mt-3"
                                    onClick={() => removeItem(item.id)}
                                >
                                    Remove
                                </button>

                            </div>

                        </div>

                    ))
            }

        </div>

    );

}

export default Wishlist;