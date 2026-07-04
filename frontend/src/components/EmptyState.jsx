import { Link } from "react-router-dom";

function EmptyState({

    icon,
    title,
    message,
    buttonText,
    buttonLink

}) {

    return (

        <div className="text-center py-5">

            <h1 style={{ fontSize: "90px" }}>

                {icon}

            </h1>

            <h3>

                {title}

            </h3>

            <p className="text-muted">

                {message}

            </p>

            {

                buttonText &&

                <Link
                    to={buttonLink}
                    className="btn btn-primary rounded-pill mt-3 px-4"
                >

                    {buttonText}

                </Link>

            }

        </div>

    );

}

export default EmptyState;