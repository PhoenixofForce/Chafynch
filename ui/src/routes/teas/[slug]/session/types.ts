// Todo: should come from the backend

export interface Infusion {
	startTime: Date;
	infusionTime?: number;
	rating?: number;
	tastingNotes: Record<string, string[]>;
}

export interface Session {
	infusions: Infusion[];
}

export const categories = [
	{ name: 'Eye', subCategories: ['Wet Leaf', 'Liquor'] },
	{ name: 'Nose', subCategories: ['Wet Leaf', 'Liquor', 'Empty Cup'] },
	{ name: 'Mouth', subCategories: ['Texture', 'Taste', 'Finish'] }
];
