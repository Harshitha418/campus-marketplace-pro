function LoadingSpinner({ message = "Loading..." }) {

    return (

        <div
            className="d-flex flex-column justify-content-center align-items-center"
            style={{ height: "60vh" }}
        >

            <div
                className="spinner-border text-primary mb-3"
                role="status"
            >
                <span className="visually-hidden">

                    Loading...

                </span>
            </div>

            <h5 className="text-muted">

                {message}

            </h5>

        </div>

    );

}

export default LoadingSpinner;