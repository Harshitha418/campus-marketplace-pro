import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../services/api";

function ProductDetails() {

    const { id } = useParams();

    const [product, setProduct] = useState(null);

    useEffect(() => {

        loadProduct();

    }, []);

    const loadProduct = async () => {

        try {

            const response = await api.get(`/products/${id}`);

            setProduct(response.data);

        } catch (error) {

            console.log(error);

            alert("Failed to load product");

        }

    };

    if (!product) {

        return (
            <div className="container mt-5">

                Loading...

            </div>
        );

    }

    return (

        <div className="container mt-5">

            <div className="card shadow-lg">

                <div className="card-body">

                    <div
                        className="text-center mb-4"
                        style={{ fontSize: "80px" }}
                    >

                        📦

                    </div>

                    <h2>

                        {product.title}

                    </h2>

                    <p>

                        {product.description}

                    </p>

                    <span className="badge bg-primary">

                        {product.category}

                    </span>

                    <h3 className="text-success mt-3">

                        ₹ {product.price}

                    </h3>

                    <h5 className="mt-3">

                        Seller

                    </h5>

                    <p>

                        {product.sellerEmail}

                    </p>

                </div>

            </div>

        </div>

    );

}

export default ProductDetails;