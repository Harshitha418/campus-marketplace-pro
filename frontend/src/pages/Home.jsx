import { useEffect, useState } from "react";
import api from "../services/api";
import ProductCard from "../components/ProductCard";

function Home() {

    const [products, setProducts] = useState([]);

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

            <h2 className="mb-4">
                Campus Marketplace
            </h2>

            <div className="row">

                {products.map((product) => (

                    <div
                        key={product.id}
                        className="col-md-4 mb-4">

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