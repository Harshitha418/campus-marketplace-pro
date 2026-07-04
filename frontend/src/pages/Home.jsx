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
            toast.error(error.message);

        }

    };

    return (

    <div className="container py-5">

        <div className="text-center mb-5">

    <h1 className="fw-bold display-5">

        Campus Marketplace Pro

    </h1>

    <p className="text-muted fs-5">

        Buy • Sell • Discover products within your campus

    </p>

</div>
        <div className="card shadow border-0 rounded-4 mb-4">

            <div className="card-body">

                <input
                    type="text"
                    className="form-control form-control-lg"
                    placeholder="🔍 Search for books, electronics, notes..."
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                />

            </div>

        </div>

        <div className="d-flex flex-wrap justify-content-center gap-2 mb-5">

            <button
                className={`btn ${
                    category === "All"
                        ? "btn-dark"
                        : "btn-outline-dark"
                }`}
                onClick={() => setCategory("All")}
            >
                All
            </button>

            <button
                className={`btn ${
                    category === "Books"
                        ? "btn-primary"
                        : "btn-outline-primary"
                }`}
                onClick={() => setCategory("Books")}
            >
                📚 Books
            </button>

            <button
                className={`btn ${
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

                        <ProductCard
                            product={product}
                        />

                    </div>

                ))}

        </div>

    </div>

);

}

export default Home;