import * as React from 'react';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import { Container } from '@mui/system';
import { Paper, Button } from '@mui/material';
import Grid from "@mui/material/Grid";
import MenuItem from '@mui/material/MenuItem';


export default function Item() {

    //item
const paperStyle = {padding: '50px 20 px', width:500, margin:"10px auto"}
const [id, setId] = React.useState('')
const [name, setName] = React.useState('')
const [price, setPrice] = React.useState('');
const [description, setDescription] = React.useState('')
const [stock, setStock] = React.useState('')
const [availableStock, setAvailableStock] = React.useState('')
const [items, setItems] = React.useState([]) 

    //warehouse
const [warehouseIdFrom, setWarehouseIdFrom] = React.useState('')
const [warehouseIdTo, setWarehouseIdTo] = React.useState('')
const [warehouseName, setWarehouseName] = React.useState('')
const [location, setLocation] = React.useState('');
const [warehouses, setWarehouses] = React.useState([]) 
const [itemId, setItemId] = React.useState('')
const [quantityToMove, setQuantityToMove] = React.useState(0)


var buttonText = React.useRef('Submit')
var addItemText = React.useRef('Add Item')
var itemToEdit = React.useRef('')


const addItem = (e) => {
    e.preventDefault()

    //adding a normal new item
    if(addItemText.current === 'Add Item') {
        const item = {name, price, description, stock}
        
        fetch("http://localhost:8080/item/add", {
            method:"POST",
            headers:{"Content-Type":"application/json"},
            body:JSON.stringify(item)

        }).then(() => {
            console.log("New item added")
            setId('')
            setName('')
            setPrice('')
            setDescription('')
            setStock('')
            setAvailableStock('')
            updateItems()
        })
    } else {
        //updating another item

        const item = {id, name, price, description, stock}
        
        fetch(`http://localhost:8080/item/${id}`, {
            method:"PUT",
            headers:{"Content-Type":"application/json"},
            body:JSON.stringify(item)

        }).then(() => {
            console.log("item updated")
            buttonText.current = 'Submit'
            addItemText.current = 'Add Item'
            setId('')
            setName('')
            setPrice('')
            setDescription('')
            setStock('')
            setAvailableStock('')
            updateItems()
        })
    }
}

const removeItem = index => {
    // e.preventDefault()
    // const item = {name, price, description, stock}
    // console.log(item)
    fetch(`http://localhost:8080/item/${index}`, {
        method:"DELETE"
    
    }).then(() => {
        console.log("Item deleted")
        updateItems();
        updateWarehouses()
    })
}

const editItem = index => {
    items.forEach((item, i) => {
        if(item.id === index) { 
            itemToEdit.current = item
            setId(item.id)
            setName(item.name)
            setPrice(item.price)
            setDescription(item.description)
            setStock(item.stock)
            setAvailableStock(item.availableStock)
            
            buttonText.current = 'Update Item'
            addItemText.current = 'Edit Item'
        }
    })
    console.log(itemToEdit.current);
    console.log(items)
}

var itemsUpdated = React.useRef(false);

const updateItems = () => {

    fetch("http://localhost:8080/item/getAll")
    .then(res => res.json())
    .then((result) => {
        setItems(result)
    })
}

if(!itemsUpdated.current) { 
    updateItems() 
    itemsUpdated.current = true;
}

const renameKey = (obj, oldKey, newKey) => {
    obj[newKey] = obj[oldKey]
    delete obj[oldKey]
    return obj
}

const addWarehouse = (e) => {
    e.preventDefault()
    const warehouse = {warehouseName, location}
    
    fetch("http://localhost:8080/warehouse/add", {
        method:"POST",
        headers:{"Content-Type":"application/json"},
        body:JSON.stringify(renameKey(warehouse, 'warehouseName', 'name'))

    }).then(() => {
        console.log("New warehouse added")
        setWarehouseName('')
        setLocation('')
        updateWarehouses()
    })
}

const removeWarehouse = index => {
    fetch(`http://localhost:8080/warehouse/${index}`, {
        method:"DELETE"
    
    }).then(() => {
        console.log("Warehouse deleted")
        updateWarehouses()
        updateItems()
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
        console.log(result)
    })
}

if(!warehousesUpdated.current) { 
    updateWarehouses() 
    warehousesUpdated.current = true;
}

const moveItem = () => {
    console.log(`moving ${quantityToMove} items ${itemId} from ${warehouseIdFrom} to ${warehouseIdTo}`)
    if(quantityToMove > 0){
        if(warehouseIdFrom === '') {
            //just adding items simply to a warehouse
            fetch(`http://localhost:8080/warehouse/addItem?warehouseId=${warehouseIdTo}&itemId=${itemId}&stock=${quantityToMove}`)
            .then(() => {
                setItemId('')
                setWarehouseIdTo('')
                setWarehouseIdFrom('')
                setQuantityToMove(0)
                updateWarehouses()
                updateItems()
            })

        } else {
            //transitioning items
            fetch(`http://localhost:8080/warehouse/moveItem?warehouseFromId=${warehouseIdFrom}&warehouseToId=${warehouseIdTo}&itemId=${itemId}&stock=${quantityToMove}`)
            .then(res => res.json())
            .then((result) => {
                console.log(result)
                setItemId('')
                setWarehouseIdTo('')
                setWarehouseIdFrom('')
                setQuantityToMove(0)
                updateWarehouses()
                updateItems()
            })
        }
    }
}

const removeItemFromWarehouse = () => {
    fetch(`http://localhost:8080/warehouse/removeItem?warehouseId=${warehouseIdTo}&itemId=${itemId}&stock=${quantityToMove}`)
    .then(() => {
        setItemId('')
        setWarehouseIdTo('')
        setWarehouseIdFrom('')
        setQuantityToMove(0)
        updateWarehouses()
        updateItems()
    })
}

const handleChangeFrom = (event: React.ChangeEvent<HTMLInputElement>) => {
    setWarehouseIdFrom(event.target.value);
}

const handleChangeTo = (event: React.ChangeEvent<HTMLInputElement>) => {
    setWarehouseIdTo(event.target.value);
}


  return (
    <Grid container justify="center" >
        <div>
        <Container>
            <Paper elevation = {3} style = {paperStyle}>
                <h1>{addItemText.current}</h1>
                <Box
                component="form"
                sx={{
                    '& > :not(style)': { m: 1 },
                }}
                noValidate
                autoComplete="off"
                >
                <TextField id="outlined-basic" label="Item name" variant="outlined" fullWidth
                value = {name}
                onChange={(e) => setName(e.target.value)}
                />
                <TextField id="outlined-basic" label="Item price" variant="outlined" fullWidth
                value = {price}
                onChange={(e) => setPrice(e.target.value)}
                />
                <TextField id="outlined-basic" label="Item description" variant="outlined" fullWidth
                value = {description}
                onChange={(e) => setDescription(e.target.value)}
                />
                <TextField id="outlined-basic" label="Item stock" variant="outlined" fullWidth
                value = {stock}
                onChange={(e) => setStock(e.target.value)}
                />
                <Button variant="contained" onClick = {(e) => { addItem(e); }}>{buttonText.current}</Button>
                </Box>
            </Paper>

            <Paper elevation={3} style={paperStyle}>
                <h1>Items</h1>

                {items.map(item => (
                    <Paper elevation={6} style={{margin:"10px", padding:"15px", textAlign:"left"}} key = {item.id}>
                        Id: {item.id} <br/>
                        Name: {item.name} <br/>
                        Price: {item.price} <br/>
                        Description: {item.description}<br/>
                        Total stock: {item.stock}<br/>
                        Free stock to move: {item.availableStock}<br/>
                        <Button variant="contained" onClick = {() =>{ removeItem(item.id); }}>Delete</Button>
                        <Button variant="contained" onClick = {() =>{ editItem(item.id); }}>Edit</Button>
                    </Paper>
                ))}

            </Paper>
        </Container>
        </div>

        <div>
        <Container>
            <Paper elevation = {3} style = {paperStyle}>
                <h1>Move Items</h1>
                <Box
                component="form"
                sx={{
                    '& > :not(style)': { m: 1 },
                }}
                noValidate
                autoComplete="off"
                >
                <TextField id="outlined-basic" label="Item id" variant="outlined" fullWidth
                value = {itemId}
                onChange={(e) => setItemId(e.target.value)}
                ></TextField>
                <TextField
                id="outlined-select-warehouse-to"
                select
                label="Select"
                value={warehouseIdTo}
                onChange={handleChangeTo}
                helperText="Warehouse (destination)"
                >
                {warehouses.map((option) => (
                    <MenuItem key={option.id} value={option.id}>
                    {option.name}
                    </MenuItem>
                ))}
                </TextField>
                <TextField
                id="outlined-select-warehouse-from"
                select
                label="Select"
                value={warehouseIdFrom}
                onChange={handleChangeFrom}
                helperText="Warehouse (origin)"
                >
                {warehouses.map((option) => (
                    <MenuItem key={option.id} value={option.id}>
                    {option.name}
                    </MenuItem>
                ))}
                </TextField>
                <TextField id="outlined-basic" label="Item quantity" variant="outlined" fullWidth
                value = {quantityToMove}
                onChange={(e) => setQuantityToMove(e.target.value)}
                />
                <Button variant="contained" onClick = {(e) => { moveItem(e); }}>Move</Button>
                <Button variant="contained" onClick = {(e) => { removeItemFromWarehouse(e); }}>Remove</Button>
                </Box>
            </Paper>
        </Container>
        </div>

        <div>
        <Container>
            <Paper elevation = {3} style = {paperStyle} >
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
                value = {warehouseName}
                onChange={(e) => setWarehouseName(e.target.value)}
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
                            Stock in warehouse: {value} 
                            </Paper>
                        ))}<br/>
                        <Button variant="contained" onClick = {() =>{ removeWarehouse(warehouse.id); }}>Delete</Button>
                        {/* <Button variant="contained" onClick = {() =>{ editItem(item.id); }}>Edit</Button> */}
                    </Paper>
                ))}

            </Paper>
        </Container>
        </div>
</Grid>
    
  );
}
