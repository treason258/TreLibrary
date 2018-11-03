import React, {Component} from 'react';
import CommentItem from "./CommentItem";

class CommentList extends Component {

    static defaultProps = {
        comments111: []
    }

    render() {
        return (
            <div>
                {this.props.comments111.map((comment222, index) => {
                    return (
                        <div key={index}>
                            <CommentItem comment333={comment222}/>
                        </div>
                    )
                })}
            </div>
        )
    }
}

export default CommentList;