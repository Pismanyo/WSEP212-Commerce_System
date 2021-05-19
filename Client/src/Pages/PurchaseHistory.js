import React from "react";
import ProductEntry from "../Components/ProductEntry";
import ReviewProduct from "./ReviewProduct";
import Connection from "../API/Connection";
import ProductEntryHistory from "../Components/ProductEntryHistory";
import {CardGroup, Spinner} from "react-bootstrap";
import ProductEntryCart from "../Components/ProductEntryCart";


const products = [
    {
        name: "brioche",
        productID: 1,
        storeID: 1,
        price: 50.5,
        seller: "ma'afia",
        categories: ["pastry", "tasty"],
        rating: 5,
        numReview: 200,
    },
    {
        name: "eclair",
        productID: 2,
        storeID: 2,
        price: 50.5,
        seller: "ma'afia2",
        categories: ["pastry", "tasty"],
        rating: 4.5,
        numReview: 300,
    }
]

class PurchaseHistory extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            purchaseHistory: [],
            loaded: false,
        }

        this.handleResponse = this.handleResponse.bind(this);
    }

    handleResponse(result) {
        if (!result.isFailure) {
            this.setState({purchaseHistory: result.result, loaded: true});
        } else {
            alert(result.errMsg);
            this.props.history.goBack();
        }
    }

    componentDidMount() {
        Connection.sendGetPurchaseHistory().then(this.handleResponse, Connection.handleReject);
    }

    render() {
        const zip = (a, b) => a.map((k, i) => [k, b[i]]);
        return (
            <div>
                <h1>Purchase History</h1>
                {!this.state.loaded && <Spinner animation="grow"/>}
                {this.state.loaded && this.state.purchaseHistory.map(({basket, totalPrice, purchaseDate}) => (
                    <div>
                        <h2>Purchase from: {purchaseDate}</h2>
                        <h2>Total amount: {totalPrice}</h2>
                        {basket.map(({storeID, storeName, productsDTO, amounts}) => (
                            <CardGroup>
                                {zip(productsDTO, amounts).map( entry => (
                                    <div>
                                        <ProductEntryHistory
                                            name={entry[0].name}
                                            price={entry[0].price}
                                            seller={storeName}
                                            productID={entry[0].productID}
                                            storeID={storeID}
                                        />
                                    </div>
                                ) ) }
                            </CardGroup>
                        ))}
                    </div>
                ))}
            </div>
        );
    }
}

export default PurchaseHistory;