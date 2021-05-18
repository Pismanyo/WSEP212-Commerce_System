import React from "react";
import ProductEntry from "../Components/ProductEntry";
import {Link} from "react-router-dom";
import UpdateQuantity from "../Components/UpdateQuantity";
import Connection from "../API/Connection";
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

class Cart extends React.Component{
    constructor(props) {
        super(props);

        this.state = {
            cart: [],
            loaded: false
        }

        this.handleRemoveFromCart = this.handleRemoveFromCart.bind(this);
        this.handleQuantityChange = this.handleQuantityChange.bind(this);
        this.handleGetCartDetailsResponse = this.handleGetCartDetailsResponse.bind(this);
    }

    handleGetCartDetailsResponse(result){
        if(!result.isFailure){
            console.log("just got");
            console.log(result.result);
            this.setState({cart: result.result, loaded: true});
        }
        else{
            alert(result.errMsg);
            this.props.history.goBack();
        }
    }

    componentDidMount() {
        Connection.sendGetCartDetails().then(this.handleGetCartDetailsResponse, Connection.handleReject);
    }

    handleRemoveFromCartResponse(result){
        if(!result.response.isFailure){
            Connection.sendGetCartDetails().then(this.handleGetCartDetailsResponse, Connection.handleReject);
        }
        else{
            alert(result.response.errMsg);
        }
    }

    handleRemoveFromCart(storeID, productID){
        Connection.sendRemoveFromCart(storeID, productID).then(this.handleRemoveFromCart, Connection.handleReject);
    }

    handleQuantityChange(){
        Connection.sendGetCartDetails().then(this.handleGetCartDetailsResponse, Connection.handleReject);
    }

    render() {
        return (
            <div>
                <h1>Cart</h1>
                <p><Link to="/checkout">Checkout</Link></p>
                {this.state.loaded && this.state.cart.map(({name, productID, storeID, price,
                                                               categories, keywords, reviews, rating, numRatings}) =>(
                    <div>
                            <ProductEntryCart
                                name = {name}
                                price = {price}
                                seller = {storeID}
                                storeID = {storeID}
                                productID = {productID}
                                handler = {() => this.handleQuantityChange()}
                                handlerRemove = {() => this.handleRemoveFromCart(storeID, productID)}
                            />
                    </div>
                ) ) }
            </div>
        );
    }
}

export default Cart;