import { useEffect, useState } from "react";
import api from "../services/api";
import { getEmail } from "../services/auth";
import { FaShoppingCart, FaTrash, FaTag } from "react-icons/fa";
import { toast } from "react-toastify";

import LoadingSpinner from "../components/LoadingSpinner";
import EmptyState from "../components/EmptyState";
import ProductImage from "../components/ProductImage";

function Wishlist() {

    const [wishlist, setWishlist] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        loadWishlist();
    }, []);

    const loadWishlist = async () => {

        setLoading(true);

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
            toast.error("Failed to load wishlist");

        } finally {

            setLoading(false);

        }

    };

    const removeItem = async (id) => {

        try {

            await api.delete(`/wishlist/${id}`);

            toast.success("Removed from wishlist");

            loadWishlist();

        } catch (error) {

            console.log(error);
            toast.error("Failed to remove item");

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

            toast.success("Added to Cart!");

            loadWishlist();

        } catch (error) {

            console.log(error);
            toast.error("Failed to add to cart");

        }

    };

    if (loading) {

        return (

            <LoadingSpinner
                message="Loading wishlist..."
            />

        );

    }

    return (

        <div className="container py-5">

            <h2 className="fw-bold mb-4">

                ❤️ My Wishlist

            </h2>

            {

                wishlist.length === 0 ?

                    <EmptyState

                        icon="❤️"

                        title="Your wishlist is empty"

                        message="Save your favourite products here."

                        buttonText="Browse Products"

                        buttonLink="/"

                    />

                    :

                    wishlist.map((item) => (

                        <div
                            key={item.id}
                            className="card border-0 shadow rounded-4 mb-4"
                        >

                            <div className="card-body">

                                <div className="row align-items-center">

                                    <div className="col-md-2 text-center">

                                        <ProductImage
                                            title={item.title}
                                        />

                                    </div>

                                    <div className="col-md-7">

                                        <h4 className="fw-bold">

                                            {item.title}

                                        </h4>

                                        <p className="text-muted">

                                            {item.description}

                                        </p>

                                        <span className="badge bg-primary rounded-pill">

                                            <FaTag className="me-2" />

                                            {item.category}

                                        </span>

                                        <h4 className="text-success mt-3 fw-bold">

                                            ₹ {item.price}

                                        </h4>

                                    </div>

                                    <div className="col-md-3 text-end">

                                        <button
                                            className="btn btn-success rounded-pill w-100 mb-2"
                                            onClick={() => moveToCart(item.productId)}
                                        >

                                            <FaShoppingCart className="me-2" />

                                            Move to Cart

                                        </button>

                                        <button
                                            className="btn btn-outline-danger rounded-pill w-100"
                                            onClick={() => removeItem(item.id)}
                                        >

                                            <FaTrash className="me-2" />

                                            Remove

                                        </button>

                                    </div>

                                </div>

                            </div>

                        </div>

                    ))

            }

        </div>

    );

}

export default Wishlist;