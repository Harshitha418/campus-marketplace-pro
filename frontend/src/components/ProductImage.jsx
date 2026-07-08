function ProductImage({ title, imageUrl }) {

    const fallback = "https://placehold.co/250x250?text=Product";

    return (
        <div
            style={{
                width: "100%",
                height: "220px",
                overflow: "hidden",
                borderRadius: "12px",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                backgroundColor: "#f8f9fa"
            }}
        >
            <img
                src={imageUrl || fallback}
                alt={title}
                referrerPolicy="no-referrer"
                style={{
                    width: "100%",
                    height: "100%",
                    objectFit: "contain"
                }}
                onError={(e) => {
                    e.target.onerror = null;
                    e.target.src = fallback;
                }}
            />
        </div>
    );
}

export default ProductImage;