// Todo: should come from the backend

export interface Infusion {
	startTime: Date;
	infusionTime?: number;
	temperature?: number;
	rating?: number;
	tastingNotes: Record<string, string[]>;
	isRinse?: boolean;
}

export interface Session {
	infusions: Infusion[];
}

export const categories = [
	{ name: 'Eye', subCategories: ['Wet Leaf', 'Liquor'] },
	{ name: 'Nose', subCategories: ['Wet Leaf', 'Liquor', 'Empty Cup'] },
	{ name: 'Mouth', subCategories: ['Texture', 'Taste', 'Finish'] }
];

export type Tabs =
	| {
			tab: 'infusion';
			index: number;
	  }
	| {
			tab: 'start';
	  }
	| {
			tab: 'end';
	  };
