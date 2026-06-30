import { useEffect, useState } from "react";
import api from "../services/api";
import ProductCard from "../components/ProductCard";

function Home() {

    const [products, setProducts] = useState([]);
    const [search, setSearch] = useState("");
    const [category, setCategory] = useState("All");

    useEffect(() => {
        loadProducts();
    }, []);

    const loadProducts = async () => {

        try {

            const response = await api.get("/products");

            console.log(response.data);

            setProducts(response.data);

        } catch (error) {

            console.log("ERROR");
            console.log(error);
            alert(error.message);

        }

    };

    return (

        <div className="container mt-5">

            <div className="text-center mb-5">

                <h1 className="fw-bold">
                    🛍️ Campus Marketplace Pro
                </h1>

                <p className="text-muted">
                    Buy and Sell Products Within Your Campus
                </p>

            </div>

            <div className="mb-4">

                <input
                    type="text"
                    className="form-control"
                    placeholder="🔍 Search products..."
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                />

            </div>

            <div className="mb-4">

                <button
                    className={`btn me-2 ${
                        category === "All"
                            ? "btn-dark"
                            : "btn-outline-dark"
                    }`}
                    onClick={() => setCategory("All")}
                >
                    All
                </button>

                <button
                    className={`btn me-2 ${
                        category === "Books"
                            ? "btn-primary"
                            : "btn-outline-primary"
                    }`}
                    onClick={() => setCategory("Books")}
                >
                    📚 Books
                </button>

                <button
                    className={`btn me-2 ${
                        category === "Electronics"
                            ? "btn-success"
                            : "btn-outline-success"
                    }`}
                    onClick={() => setCategory("Electronics")}
                >
                    💻 Electronics
                </button>

                <button
                    className={`btn ${
                        category === "Notes"
                            ? "btn-warning"
                            : "btn-outline-warning"
                    }`}
                    onClick={() => setCategory("Notes")}
                >
                    📝 Notes
                </button>

            </div>

            <div className="row">

                {products
                    .filter((product) =>
                        product.title
                            .toLowerCase()
                            .includes(search.toLowerCase())
                    )
                    .filter((product) =>
                        category === "All"
                            ? true
                            : product.category === category
                    )
                    .map((product) => (

                        <div
                            key={product.id}
                            className="col-lg-4 col-md-6 mb-4"
                        >

                            <ProductCard product={product} />

                        </div>

                    ))}

            </div>

        </div>

    );

}

export default Home;