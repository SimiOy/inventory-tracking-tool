import * as React from 'react';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import { Container } from '@mui/system';
import { Paper, Button } from '@mui/material';


export default function Warehouse() {

const paperStyle = {padding: '50px 20 px', width:500, margin:"10px auto"}
const [id, setId] = React.useState('')
const [name, setName] = React.useState('')
const [location, setLocation] = React.useState('');
const [warehouses, setWarehouses] = React.useState([]) 


const addWarehouse = (e) => {
    e.preventDefault()
    const warehouse = {name, location}
    
    fetch("http://localhost:8080/warehouse/add", {
        method:"POST",
        headers:{"Content-Type":"application/json"},
        body:JSON.stringify(warehouse)

    }).then(() => {
        console.log("New warehouse added")
        setId('')
        setName('')
        setLocation('')
        updateWarehouses()
    })
}

const removeWarehouse = index => {
    fetch(`http://localhost:8080/warehouse/${index}`, {
        method:"DELETE"
    
    }).then(() => {
        console.log("Warehouse deleted")
        updateWarehouses();
    })
}


const getItemsFromWarehouse = (wh) => {

    fetch(`http://localhost:8080/warehouse/getItems?warehouseId=${wh.id}`)
    .then(res => res.json())
    .then((result) => {
        wh.items = result
    })
}

var warehousesUpdated = React.useRef(false);

const updateWarehouses = () => {

    fetch("http://localhost:8080/warehouse/getAll")
    .then(res => res.json())
    .then((result) => {
        setWarehouses(result)
    })
}

if(!warehousesUpdated.current) { 
    updateWarehouses() 
    warehousesUpdated.current = true;
}


  return (
    <Container>
        <Paper elevation = {3} style = {paperStyle}>
            <h1>Warehouses</h1>
            <Box
            component="form"
            sx={{
                '& > :not(style)': { m: 1 },
            }}
            noValidate
            autoComplete="off"
            >
            <TextField id="outlined-basic" label="Warehouse name" variant="outlined" fullWidth
            value = {name}
            onChange={(e) => setName(e.target.value)}
            />
            <TextField id="outlined-basic" label="Warehouse location" variant="outlined" fullWidth
            value = {location}
            onChange={(e) => setLocation(e.target.value)}
            />
            <Button variant="contained" onClick = {(e) => { addWarehouse(e); }}>Add warehouse</Button>
            </Box>
        </Paper>

        <Paper elevation={3} style={paperStyle}>
            <h1>Warehouses</h1>

            {warehouses.map(warehouse => (
                <Paper elevation={6} style={{margin:"10px", padding:"15px", textAlign:"left"}} key = {warehouse.id}>
                    Id: {warehouse.id} <br/>
                    Name: {warehouse.name} <br/>
                    Location: {warehouse.location} <br/>
                    Items: {Object.entries(warehouse.items).map(([key, value]) => (
                        <Paper elevation={6} style={{margin:"10px", padding:"15px", textAlign:"left"}} key = {key}>
                        Id: {key} <br/>
                        Name: { } <br/>
                        Stock in warehouse: {value} 
                        </Paper>
                    ))}<br/>
                    <Button variant="contained" onClick = {() =>{ removeWarehouse(warehouse.id); }}>Delete</Button>
                    {/* <Button variant="contained" onClick = {() =>{ editItem(item.id); }}>Edit</Button> */}
                </Paper>
            ))}

        </Paper>
    </Container>
  );
}