export const addItemAction = 'ADD_ITEM';
export const regularUpdateItemAction = 'REGULAR_UPDATA_ITEM';
export const modifyItemAction = 'MODIFY_ITEM';


export function getAction(type, data) {
  return {
    type, data
  }
}