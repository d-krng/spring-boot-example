const UserProfile = ({name, age, gender, imageNumber, ...props}) => {

    gender = gender === "MALE" ? "men" : "women"

    return (
        <div>
            <h2>{name}</h2>
            <p>{age}</p>
            <img src={`https://randomuser.me/api/portraits/${gender}/${imageNumber}.jpg`}/>
            {props.children}
        </div>
    )
}

export default UserProfile;
