import {Text, Spinner, Wrap, WrapItem} from '@chakra-ui/react'
import SidebarWithHeader from "./components/shared/Sidebar.jsx";
import {useEffect, useState} from "react";
import {getCustomers} from "./services/client.js";
import CardWithImage from "./components/Card.jsx";

function App() {

    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(true);
        getCustomers().then(res => {
            setCustomers(res.data)
        }).catch(err => {
            console.log(err);
        }).finally(() => {
            setLoading(false)
        })
    }, []);

    if (loading) {
        return (
            <SidebarWithHeader>
                <Spinner
                    thickness='4px'
                    speed='0.65s'
                    emptyColor='gray.200'
                    color='blue.500'
                    size='xl'
                />
            </SidebarWithHeader>
        )
    }

    if (customers.length <= 0) {
        return (
            <SidebarWithHeader>
                <Text>No customers available</Text>
            </SidebarWithHeader>
        )
    }
    return (
        <SidebarWithHeader>
            <Wrap spacing={'100px'} justify={'space-around'}>
            {customers.map((customer, index) => (
                    <WrapItem key={index}>
                        <CardWithImage{...customer}></CardWithImage>
                    </WrapItem>

            ))}
            </Wrap>
        </SidebarWithHeader>

    )

}

export default App
